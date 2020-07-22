package com.antique.auction.controllers;

import com.antique.auction.models.Item;
import com.antique.auction.models.ItemPrice;
import com.antique.auction.repositories.ItemPriceRepository;
import com.antique.auction.repositories.ItemRepository;
import com.antique.auction.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class ItemsController {
    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final ItemsService itemsService;

    @Autowired
    public ItemsController(ItemRepository itemRepository, ItemPriceRepository itemPriceRepository,
                           ItemsService itemsService) {
        this.itemRepository = itemRepository;
        this.itemPriceRepository = itemPriceRepository;
        this.itemsService = itemsService;
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
        Item item = itemRepository.findById(id).orElse(new Item());
        ModelAndView modelAndView = new ModelAndView("item-details", "item", item);
        return modelAndView;
    }

    @PostMapping(value = "/item/bid/add/{id}")
    public ModelAndView addBid(@PathVariable int id, Item item) {
        Item existingItem;
        if (item.getId() != null) {
            existingItem = itemRepository.findById(id).orElse(null);
        } else {
            existingItem = new Item();
        }
        if (item.getCurrentPrice() <= (existingItem != null ? existingItem.getCurrentPrice() : 0)) {
            ModelAndView modelAndView = new ModelAndView("item-details", "item", existingItem);
            modelAndView.addObject("wrongBid", "true");
            return modelAndView;
        }
        existingItem.setCurrentPrice(item.getCurrentPrice());
        ItemPrice currentItemPrice = new ItemPrice();
        currentItemPrice.setItem(existingItem);
        currentItemPrice.setPriceValue(item.getCurrentPrice());
        existingItem.getItemPrices().add(currentItemPrice);
        itemPriceRepository.save(currentItemPrice);
        itemRepository.save(existingItem);
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @RequestMapping(value = {"/", "/home"})
    public ModelAndView home(ModelAndView modelAndView) {
        return populateModelAndView(modelAndView, Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    //todo to remove of fix
    @RequestMapping("/goToUploadImage")
    public ModelAndView uploadImage(ModelAndView modelAndView) {
        modelAndView.setViewName("upload-image");
        return modelAndView;
    }


    @PostMapping(value = "/item/addEdit/{id}")
    public ModelAndView editItem(@PathVariable int id, Item item) {
        fixItemDoubleDateParam(item);
        itemRepository.save(item);
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @PostMapping(value = "/item/addEdit")
    public ModelAndView addItem(Item item) {
        //todo multiple values are passed here... To check and fix
        fixItemDoubleDateParam(item);
        itemRepository.save(item);
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @GetMapping(value = "/item/delete/{id}")
    public ModelAndView addItem(@PathVariable Integer id) {
        itemRepository.deleteById(id);
        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
    }

    @GetMapping("/item/edit/view/{id}")
    public ModelAndView editItem(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Item existingItem = itemRepository.findById(id).orElse(null);
        modelAndView.addObject("item", existingItem);
        modelAndView.setViewName("add-edit-item");
        return modelAndView;
    }

    @GetMapping("/item/add/view")
    public ModelAndView addItem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new Item());
        modelAndView.setViewName("add-edit-item");
        return modelAndView;
    }
//todo to fix upload

//    @PostMapping(value = "/image")
//    public ModelAndView getImage() throws IOException {
//
//        File serverFile = new File("/static/" + "some name" + ".jpg");
//
//        Files.readAllBytes(serverFile.toPath());
//        return populateModelAndView(new ModelAndView(), Optional.of(1), Optional.of(10), Optional.empty(), Optional.empty());
//    }
//
//    @RequestMapping(value="/savefile",method=RequestMethod.POST)
//    public ModelAndView upload(@RequestParam CommonsMultipartFile file, HttpSession session){
//        String path=session.getServletContext().getRealPath("/");
//        String filename=file.getOriginalFilename();
//
//        System.out.println(path+" "+filename);
//        try{
//            byte barr[]=file.getBytes();
//
//            BufferedOutputStream bout=new BufferedOutputStream(
//                    new FileOutputStream(path+"/"+filename));
//            bout.write(barr);
//            bout.flush();
//            bout.close();
//
//        }catch(Exception e){System.out.println(e);}
//        return new ModelAndView("upload-success","filename",path+"/"+filename);
//    }

    private ModelAndView populateModelAndView(ModelAndView modelAndView, @RequestParam("page") Optional<Integer> page,
                                              @RequestParam("size") Optional<Integer> size, Optional<String> searchParam, Optional<String> sortOrder) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Item> itemPage = itemsService.findPaginated(PageRequest.of(currentPage - 1, pageSize), searchParam, sortOrder);
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

    private void fixItemDoubleDateParam(Item item) {
        int indexOfComma = item.getDateString().indexOf(',');
        if (indexOfComma != -1) {
            item.setDateString(item.getDateString().substring(0, indexOfComma));
        }
    }

}
