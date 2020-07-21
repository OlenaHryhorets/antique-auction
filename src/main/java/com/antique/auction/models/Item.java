package com.antique.auction.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private int currentPrice;
    private String dateString;
    private LocalDateTime bidDate;
    private Integer bidHours;

    @OneToMany(mappedBy="item",cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    private List<ItemPrice> itemPrices = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public LocalDateTime getBidDate() {
        return bidDate;
    }

    public void setBidDate(LocalDateTime bidDate) {
        this.bidDate = bidDate;
    }

    public Integer getBidHours() {
        return bidHours;
    }

    public void setBidHours(Integer bidHours) {
        this.bidHours = bidHours;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
