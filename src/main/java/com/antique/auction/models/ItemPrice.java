package com.antique.auction.models;

import javax.persistence.*;

@Entity
public class ItemPrice {

    @Id
    @GeneratedValue
    private int id;
    private int priceValue;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinTable(name="item_prices",joinColumns=@JoinColumn(name="id"),inverseJoinColumns=@JoinColumn(name="item_id"))
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
}
