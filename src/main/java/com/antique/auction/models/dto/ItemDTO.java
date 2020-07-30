package com.antique.auction.models.dto;

import java.util.List;

public class ItemDTO {
    private Integer id;
    private String currentPrice;
    private Integer finalPrice;
    private String finalPriceUserName;
    private List<String> bidUsers;
    private String name;
    private String statusName;

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


}
