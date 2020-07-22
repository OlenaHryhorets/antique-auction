package com.antique.auction.services;

import com.antique.auction.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemService {
    Item findById(int id);

    void save(Item existingItem);

    void deleteById(Integer id);

    Page<Item> findPaginated(Pageable pageable, Optional<String> searchParam, Optional<String> sortOrder);
}
