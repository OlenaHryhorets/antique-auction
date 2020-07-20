package com.antique.auction.repositories;

import com.antique.auction.models.ItemPrice;
import org.springframework.data.repository.CrudRepository;

public interface ItemPriceRepository extends CrudRepository<ItemPrice, Integer> {
}
