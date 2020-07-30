package com.antique.auction.repositories;

import com.antique.auction.models.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByNameContainingOrDescriptionContaining(String nameParam, String descriptionParam, Sort sort);
    List<Item> findAllByDateStringIsNotNullAndCurrentPriceIsNotNullAndAwardedFalse();
}
