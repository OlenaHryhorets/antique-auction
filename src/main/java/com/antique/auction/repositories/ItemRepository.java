package com.antique.auction.repositories;

import com.antique.auction.models.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Integer> {
    List<Item> findByNameContainingOrDescriptionContainingOrderByCurrentPriceAsc(String param, String param2);
    List<Item> findByNameContainingOrDescriptionContainingOrderByCurrentPriceDesc(String param, String param2);
    List<Item> findByNameContainingOrDescriptionContaining(String param, String param2);
    List<Item> findAllByOrderByCurrentPriceAsc();
    List<Item> findAllByOrderByCurrentPriceDesc();
}
