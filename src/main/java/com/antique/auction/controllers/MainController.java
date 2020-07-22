package com.antique.auction.controllers;

import com.antique.auction.models.Item;
import com.antique.auction.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    private final ItemRepository itemRepository;

    @Autowired
    public MainController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * This method is used to generate demo data, authenticate  and then head to localhost:8080/start
     * @return the name of home view
     */
    @GetMapping("/start")
    @ResponseBody
    public String start () {
        itemRepository.save(getItem("demoItem1", "demo description 1", 10));
        itemRepository.save(getItem("demoItem2", "demo description 21", 20));
        itemRepository.save(getItem("demoItem3", "demo description 31", 100));
        itemRepository.save(getItem("demoItem4", "demo description 41", 15));
        itemRepository.save(getItem("demoItem5", "demo description 51", 10));
        itemRepository.save(getItem("demoItem6", "demo description 61", 35));
        itemRepository.save(getItem("demoItem7", "demo description 71", 11));
        itemRepository.save(getItem("demoItem8", "demo description 81", 30));
        itemRepository.save(getItem("demoItem9", "demo description 91", 10));
        itemRepository.save(getItem("demoItem10", "demo description 101", 10));
        itemRepository.save(getItem("demoItem11", "demo description 102", 55));
        return "Items are generated";
    }

    private Item getItem(String name, String description, int price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCurrentPrice(price);
        return item;
    }


    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
