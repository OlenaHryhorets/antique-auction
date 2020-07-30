package com.antique.auction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Bid {

    @Id
    @GeneratedValue
    private int id;
    private int priceValue;
    private LocalDateTime bidDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "item_bids", joinColumns = @JoinColumn(name = "bid_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Item item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(int priceValue) {
        this.priceValue = priceValue;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getBidDate() {
        return bidDate;
    }

    public void setBidDate(LocalDateTime bidDate) {
        this.bidDate = bidDate;
    }
}
