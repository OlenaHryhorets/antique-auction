package com.antique.auction.services.impl;

import com.antique.auction.models.ItemPrice;
import com.antique.auction.repositories.ItemPriceRepository;
import com.antique.auction.services.ItemPriceService;
import org.springframework.stereotype.Service;

@Service
public class ItemPriceServiceImpl implements ItemPriceService {
    private final ItemPriceRepository itemPriceRepository;

    public ItemPriceServiceImpl(ItemPriceRepository itemPriceRepository) {
        this.itemPriceRepository = itemPriceRepository;
    }

    @Override
    public void save(ItemPrice currentItemPrice) {
        itemPriceRepository.save(currentItemPrice);
    }
}
