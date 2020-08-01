package com.antique.auction.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ItemDTO {
    private Integer id;
    private String itemName;
    private String itemDescription;
    private String currentPrice;
    private Integer finalPrice;
    private String finalPriceUserName;
    private List<String> bidUsers;
    private String name;
    private String statusName;
    private List<Integer> bidPrices;
    private String dateStringValue;
    private LocalDateTime currentBidDate;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getFinalPriceUserName() {
        return finalPriceUserName;
    }

    public void setFinalPriceUserName(String finalPriceUserName) {
        this.finalPriceUserName = finalPriceUserName;
    }

    public List<String> getBidUsers() {
        return bidUsers;
    }

    public void setBidUsers(List<String> bidUsers) {
        this.bidUsers = bidUsers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<Integer> getBidPrices() {
        return bidPrices;
    }

    public void setBidPrices(List<Integer> bidPrices) {
        this.bidPrices = bidPrices;
    }

    public String getDateStringValue() {
        return dateStringValue;
    }

    public void setDateStringValue(String dateStringValue) {
        this.dateStringValue = dateStringValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public LocalDateTime getCurrentBidDate() {
        return currentBidDate;
    }

    public void setCurrentBidDate(LocalDateTime currentBidDate) {
        this.currentBidDate = currentBidDate;
    }
}
