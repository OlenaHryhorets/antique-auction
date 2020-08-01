package com.antique.auction.controllers;

import com.antique.auction.email.EmailService;
import com.antique.auction.models.Bid;
import com.antique.auction.models.Item;
import com.antique.auction.models.User;
import com.antique.auction.models.dto.ItemDTO;
import com.antique.auction.repositories.UserRepository;
import com.antique.auction.services.BidService;
import com.antique.auction.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class ItemsController {
    private static final String ANTIQUE_AUCTION_IMAGES_DIR_NAME = "antique-auction-images";
    private final ItemService itemService;
    private final BidService bidService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${user.home}")
    public String uploadDir;

    @Autowired
    public ItemsController(ItemService itemsService, BidService bidService, EmailService emailService,
                           UserRepository userRepository) {
        this.itemService = itemsService;
        this.bidService = bidService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/items")
    public ModelAndView getItems(ModelAndView modelAndView,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size,
                                 @RequestParam("searchParam") Optional<String> searchParam,
                                 @RequestParam("sortOrder") Optional<String> sortOrder) {
        return populateModelAndView(modelAndView, page, size, searchParam, sortOrder);
    }

    @GetMapping(value = "/item/details/{id}")
    public ModelAndView gotToItemsDetail(@PathVariable int id) {
        Item item = itemService.findById(id);
        return new ModelAndView("item-details", "item", item);
    }

    @GetMapping(value = "/item/status/get/{itemId}", produces = {"application/json"})
    public ItemDTO getItemStatus(@PathVariable int itemId) {
        itemService.finalizeBids();
        Item item = itemService.findById(itemId);
        ItemDTO itemDto = new ItemDTO();
        if (item.getCurrentPrice() != null) {
            itemDto.setCurrentPrice(String.valueOf(item.getCurrentPrice()));
        }
        itemDto.setItemName(item.getName());
        itemDto.setItemDescription(item.getDescription());
        itemDto.setDateStringValue(item.getDateString());
        itemDto.setFinalPrice(item.getCurrentPrice());
        itemDto.setFinalPriceUserName(item.getBidUserLogin());
        itemDto.setBidPrices(item.getBids().stream().map(Bid::getPriceValue).collect(Collectors.toList()));
        return itemDto;
    }

    @PostMapping(value = "/item/bid/add/{id}")
    public ModelAndView addBid(@PathVariable int id, Item item) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByLogin(principal.getUsername());
        Item existingItem;
        if (item.getId() != null) {
            existingItem = itemService.findById(id);
        } else {
            existingItem = new Item();
        }
        existingItem.addUser(currentUser);
        if (item.getCurrentPrice().compareTo(existingItem.getCurrentPrice() != null ? existingItem.getCurrentPrice() : 0) <= 0) {
            ModelAndView modelAndView = new ModelAndView("item-details", "item", existingItem);
            modelAndView.addObject("wrongBid", "true");
            return modelAndView;
        }
        existingItem.setCurrentPrice(item.getCurrentPrice());
        existingItem.setCurrentBidDate(LocalDateTime.now());
        Bid currentBid = new Bid();
        currentBid.setItem(existingItem);
        currentBid.setBidDate(LocalDateTime.now());
        currentBid.setPriceValue(item.getCurrentPrice());
        existingItem.getBids().add(currentBid);
        bidService.save(currentBid);
        currentUser.addItem(item);
        userRepository.save(currentUser);
        existingItem.setBidUserLogin(currentUser.getLogin());
        itemService.save(existingItem);
        existingItem.getUsers().stream().filter(user -> !user.equals(currentUser)).forEach(user -> {
            //send email to bid user
            Map<String, Object> params = new HashMap<>();
            params.put("itemName", existingItem.getName());
            params.put("bid", item.getCurrentPrice());
            try {
                emailService.sendNewBidOnItemEmail(user.getEmail(), "There is a new bid on item", params);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @RequestMapping(value = {"/", "/home"})
    public ModelAndView home(ModelAndView modelAndView) {
        return populateModelAndView(modelAndView, Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
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
        if (!StringUtils.isEmpty(item.getDateString())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime itemDate = LocalDateTime.parse(item.getDateString(), formatter);
            if (itemDate.isBefore(LocalDateTime.now()) || itemDate.isEqual(LocalDateTime.now())) {
                return getBackRedirectView(item);
            }
        } else {
            return getBackRedirectView(item);
        }
        itemService.save(item);
        return new RedirectView("/home");
    }

    private RedirectView getBackRedirectView(Item item) {
        RedirectView redirectView = new RedirectView();
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("itemName", item.getName());
        attributes.put("itemDescription", item.getDescription());
        attributes.put("wrongDate", "true");
        redirectView.setUrl("/item/edit/view");
        redirectView.setAttributesMap(attributes);
        return redirectView;
    }

    @GetMapping(value = "/item/delete/{id}")
    public RedirectView addItem(@PathVariable Integer id) {
        Item item = itemService.findById(id);
        item.getBids().forEach(bid ->
        {
            bid.setItem(null);
            bidService.save(bid);
        });
        item.getBids().clear();
        itemService.save(item);
        List<User> users = userRepository.findAllByItemsContains(item);
        users.forEach(user -> {
            user.getItems().removeAll(Collections.singletonList(item));
            userRepository.save(user);
        });
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

    @GetMapping("/item/edit/view")
    public ModelAndView editItem(@RequestParam String wrongDate, @RequestParam String itemName,
                                 @RequestParam String itemDescription) {
        ModelAndView modelAndView = new ModelAndView();
        Item item = new Item();
        item.setName(itemName);
        item.setDescription(itemDescription);
        modelAndView.addObject("item", item);
        modelAndView.addObject("wrongDate", wrongDate);
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

    private void setImage(Item item, @RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            Path copyLocation = Paths.get(uploadDir + File.separator + ANTIQUE_AUCTION_IMAGES_DIR_NAME + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            item.setImageName(filename);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
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
}
