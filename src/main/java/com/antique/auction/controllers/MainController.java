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

    @GetMapping("/start")
    public String start () {
        itemRepository.save(getItem("first", "some first", 10));
        itemRepository.save(getItem("second", "some fijujurst", 10));
        itemRepository.save(getItem("ttttt", "some firjujust", 100));
        itemRepository.save(getItem("rrrrr", "some first", 10));
        itemRepository.save(getItem("firseet", "some first", 10));
        itemRepository.save(getItem("firsewwt", "some jujuj", 10));
        itemRepository.save(getItem("first", "some first", 10));
        itemRepository.save(getItem("firdfgfdst", "some ikiki", 30));
        itemRepository.save(getItem("firfdgst", "some first", 10));
        itemRepository.save(getItem("hjhjhj", "some first", 10));
        itemRepository.save(getItem("first", "some first", 10));
        itemRepository.save(getItem("firstnvb", "some first", 10));
        itemRepository.save(getItem("fidgfdgrst", "some first", 10));
        itemRepository.save(getItem("fibnvbnrst", "some first", 50));
        itemRepository.save(getItem("first", "some first", 10));
        return "home";
    }
    private Item getItem(String name, String description, int price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
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
