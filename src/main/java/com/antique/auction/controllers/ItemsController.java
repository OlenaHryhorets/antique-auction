package com.antique.auction.controllers;

import com.antique.auction.email.EmailService;
import com.antique.auction.models.Bid;
import com.antique.auction.models.Item;
import com.antique.auction.models.Role;
import com.antique.auction.models.User;
import com.antique.auction.models.dto.ItemDTO;
import com.antique.auction.repositories.RoleRepository;
import com.antique.auction.repositories.UserRepository;
import com.antique.auction.services.BidService;
import com.antique.auction.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class ItemsController {
    private static final String ANTIQUE_AUCTION_IMAGES_DIR_NAME = "antique-auction-images";
    private final ItemService itemService;
    private final BidService bidService;
    private final EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${user.home}")
    public String uploadDir;

    @Autowired
    public ItemsController(ItemService itemsService, BidService bidService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.itemService = itemsService;
        this.bidService = bidService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/items")
    public ModelAndView getItems(ModelAndView modelAndView,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size,
                                 @RequestParam("searchParam") Optional<String> searchParam,
                                 @RequestParam("sortOrder") Optional<String> sortOrder) {
        return populateModelAndView(modelAndView, page, size, searchParam, sortOrder);
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {
        if (itemService.count() == 0) {
            addInitialDemoData();
        }
        addUsersAndRolesIfNeeded();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/sendEmail")
    public @ResponseBody String sendEmail() {
        emailService.sendSimpleMessage("adamenkolena7@gmail.com", "Test email", "This is email text HELLO!)))");
        return "Email is sent";
    }

    @GetMapping(value = "/item/details/{id}")
    public ModelAndView gotToItemsDetail(@PathVariable int id) {
        Item item = itemService.findById(id);
        return new ModelAndView("item-details", "item", item);
    }

    @GetMapping(value = "/item/status/get/{itemId}", produces = { "application/json" })
    public ItemDTO getItemStatus(@PathVariable int itemId) {
        itemService.finalizeBids();
        Item item = itemService.findById(itemId);
        ItemDTO itemDto = new ItemDTO();
        if (item.getCurrentPrice() != null) {
            itemDto.setCurrentPrice(String.valueOf(item.getCurrentPrice()));
        }
        if (item.isAwarded()) {
            itemDto.setItemAwarded();
        }
        itemDto.setFinalPrice(item.getCurrentPrice());
        List<User> users = item.getUsers();
        if (users != null && !users.isEmpty()) {
            itemDto.setFinalPriceUserName(users.get(users.size() - 1).getLogin());
        }
        return itemDto;
    }

    @PostMapping(value = "/item/bid/add/{id}")
    public ModelAndView addBid(@PathVariable int id, Item item) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByLogin(principal.getUsername());
        item.addUser(currentUser);
        Item existingItem;
        if (item.getId() != null) {
            existingItem = itemService.findById(id);
        } else {
            existingItem = new Item();
        }
        if (item.getCurrentPrice().compareTo((existingItem != null && existingItem.getCurrentPrice() != null ? existingItem.getCurrentPrice() : 0)) <= 0) {
            ModelAndView modelAndView = new ModelAndView("item-details", "item", existingItem);
            modelAndView.addObject("wrongBid", "true");
            return modelAndView;
        }
        if (existingItem != null) {
            existingItem.setCurrentPrice(item.getCurrentPrice());
        }
        Bid currentBid = new Bid();
        currentBid.setItem(existingItem);
        currentBid.setBidDate(LocalDateTime.now());
        currentBid.setPriceValue(item.getCurrentPrice());
        if (existingItem != null) {
            existingItem.getBids().add(currentBid);
        }
        bidService.save(currentBid);
        currentUser.addItem(item);
        userRepository.save(currentUser);
        if (existingItem != null) {
            existingItem.setBidUserLogin(currentUser.getLogin());
        }
        itemService.save(existingItem);
        if (existingItem != null) {
            existingItem.getUsers().stream().filter(user -> !user.equals(currentUser)).forEach(user -> {
                emailService.sendSimpleMessage(user.getEmail(), "Bid is added", "There is a new bid on item " +
                        existingItem.getName() + "; new price is - " + currentBid.getPriceValue());
            });
        }
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @RequestMapping(value = {"/", "/home"})
    public ModelAndView home(ModelAndView modelAndView) {
        return populateModelAndView(modelAndView, Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    private void addUsersAndRolesIfNeeded() {
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role userRole = createRoleIfNotFound("USER");

        User admin = userRepository.findByLogin("admin");
        if (admin == null) {
            admin = new User();
            admin.setLogin("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
//            admin.setPassword("admin");
            admin.setEmail("admin@test.com");
            admin.setEnabled(true);
            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);
        }

        User user = userRepository.findByLogin("user");
        if (user == null) {
            user = new User();
            user.setLogin("user");
            user.setFirstName("user");
            user.setLastName("user");
            user.setPassword(passwordEncoder.encode("user"));
//            user.setPassword("user");
            user.setEmail("user@test.com");
            user.setRoles(Collections.singletonList(userRole));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Transactional
    Role createRoleIfNotFound(
            String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

    @PostMapping(value = "/item/add/edit/{id}")
    public RedirectView editItem(@PathVariable int id, Item item, @RequestParam("file") MultipartFile file) {
        Item existingItem = itemService.findById(id);
        existingItem.setId(item.getId());
        existingItem.setDateString(item.getDateString());
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        if (file != null && !file.isEmpty()) {
            setImage(existingItem, file);
        }
        itemService.save(existingItem);
        return new RedirectView("/home");
    }

    @PostMapping(value = "/item/add/edit")
    public RedirectView addItem(Item item, @RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            setImage(item, file);
        }
        itemService.save(item);
        return new RedirectView("/home");
    }

    private void setImage(Item item, @RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            Path copyLocation = Paths
                    .get(uploadDir + File.separator + ANTIQUE_AUCTION_IMAGES_DIR_NAME + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            item.setImageName(filename);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/item/delete/{id}")
    public RedirectView addItem(@PathVariable Integer id) {
        itemService.deleteById(id);
        return new RedirectView("/home");
    }

    @GetMapping("/item/edit/view/{id}")
    public ModelAndView editItem(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Item existingItem = itemService.findById(id);
        modelAndView.addObject("item", existingItem);
        modelAndView.setViewName("add-edit-item");
        return modelAndView;
    }

    @GetMapping("/item/add/go")
    public ModelAndView addItem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new Item());
        modelAndView.setViewName("add-edit-item");
        return modelAndView;
    }

    @GetMapping(value = "/items/images/{id}")
    public @ResponseBody
    byte[] getImage(@PathVariable Integer id, HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Cache-Control", "max-age=60, must-revalidate, no-transform");
        Item item = itemService.findById(id);
        BufferedImage img;
        byte[] response = new byte[0];
        try {
            String imageName = item.getImageName();
            if (imageName != null && !imageName.isEmpty()) {
                img = ImageIO.read(new File(uploadDir + File.separator + ANTIQUE_AUCTION_IMAGES_DIR_NAME + File.separator + imageName));
                response = toByteArrayAutoClosable(img, "png");
            }
        } catch (IOException ignored) {
        }
        return response;
    }

    private static byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (image != null) {
                ImageIO.write(image, type, out);
            }
            return out.toByteArray();
        }
    }

    private ModelAndView populateModelAndView(ModelAndView modelAndView, @RequestParam("page") Optional<Integer> page,
                                              @RequestParam("size") Optional<Integer> size, Optional<String> searchParam, Optional<String> sortOrder) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Item> itemPage = itemService.findPaginated(PageRequest.of(currentPage - 1, pageSize), searchParam, sortOrder);
        modelAndView.addObject("itemPage", itemPage);

        int totalPages = itemPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        sortOrder.ifPresent(s -> modelAndView.addObject("sortOrder", s));
        searchParam.ifPresent(s -> modelAndView.addObject("searchParam", s));
        modelAndView.setViewName("home");
        return modelAndView;
    }

    private void addInitialDemoData() {
        saveItem("demoItem1", "demo description 1", 10);
        saveItem("demoItem2", "demo description 21", 20);
        saveItem("demoItem3", "demo description 31", 100);
        saveItem("demoItem4", "demo description 41", 15);
        saveItem("demoItem5", "demo description 51", 10);
        saveItem("demoItem6", "demo description 61", 35);
        saveItem("demoItem7", "demo description 71", 11);
        saveItem("demoItem8", "demo description 81", 30);
        saveItem("demoItem9", "demo description 91", 10);
        saveItem("demoItem10", "demo description 101", 10);
        saveItem("demoItem11", "demo description 102", 55);
    }

    private Item saveItem(String name, String description, int price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCurrentPrice(price);
        item.setImageName(name + ".png");
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        item.setDateString(LocalDateTime.now().plusHours(20).format(customFormatter));
        Bid bid = new Bid();
        bid.setPriceValue(item.getCurrentPrice());
        bid.setItem(item);
        bid.setBidDate(LocalDateTime.now());
        itemService.save(item);
        bidService.save(bid);
        return item;
    }
}
