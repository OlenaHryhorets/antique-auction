package com.antique.auction.models.dto;

import com.antique.auction.models.Item;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    List<ItemDTO> awardedItems = new ArrayList<>();

    List<ItemDTO> items = new ArrayList<>();

    public List<ItemDTO> getAwardedItems() {
        return awardedItems;
    }

    public void setAwardedItems(List<ItemDTO> awardedItems) {
        this.awardedItems = awardedItems;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
