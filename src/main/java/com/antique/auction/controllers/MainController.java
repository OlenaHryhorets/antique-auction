package com.antique.auction.controllers;

import com.antique.auction.models.Item;
import com.antique.auction.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final ItemRepository itemRepository;

    @Autowired
    public MainController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/")
    public String start () {
        Item item = new Item();
        item.setName("FirstLot");
        item.setDescription("this is first lot");
        item.setPrice(10);
        itemRepository.save(item);
        return "home";
    }
    @RequestMapping("/home")
    public String home() {
        return "home";
    }
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
