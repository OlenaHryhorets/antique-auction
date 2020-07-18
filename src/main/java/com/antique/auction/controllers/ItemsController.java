package com.antique.auction.controllers;

import com.antique.auction.models.Item;
import com.antique.auction.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemsController {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemsController(ItemRepository itemRepository) { this.itemRepository = itemRepository; }

    @GetMapping(value = "/items")
    public List<Item> getItems () {
        List<Item> result = new ArrayList<>();
        itemRepository.findAll().forEach(result::add);
        return result;
    }
}
