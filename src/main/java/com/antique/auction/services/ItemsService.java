package com.antique.auction.services;

import com.antique.auction.models.Item;
import com.antique.auction.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsService {

    private final ItemRepository itemRepository;

    public ItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> findPaginated(Pageable pageable, Optional<String> searchParam, Optional<String> sortOrder) {
        List<Item> itemsList = new ArrayList<>();
        Iterable<Item> itemIterable = null;
        if (searchParam.isPresent()) {
            if (sortOrder.isPresent()) {
                if (sortOrder.get().equals("asc")) {
                    itemIterable = itemRepository.findByNameContainingOrDescriptionContainingOrderByCurrentPriceAsc(searchParam.get(), searchParam.get());
                } else {
                    itemIterable = itemRepository.findByNameContainingOrDescriptionContainingOrderByCurrentPriceDesc(searchParam.get(), searchParam.get());
                }
            } else {
                itemIterable = itemRepository.findByNameContainingOrDescriptionContaining(searchParam.get(), searchParam.get());
            }
            itemIterable = itemRepository.findByNameContainingOrDescriptionContainingOrderByCurrentPriceAsc(searchParam.get(), searchParam.get());
        } else {
            if (sortOrder.isPresent()) {
                if (sortOrder.get().equals("asc")) {
                    itemIterable = itemRepository.findAllByOrderByCurrentPriceAsc();
                } else if (sortOrder.get().equals("desc")) {
                    itemIterable = itemRepository.findAllByOrderByCurrentPriceDesc();
                } else {
                    itemIterable = itemRepository.findAll();
                }
            } else {
                itemIterable = itemRepository.findAll();
            }
        }
        itemIterable.forEach(itemsList::add);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Item> list;

        if (itemsList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, itemsList.size());
            list = itemsList.subList(startItem, toIndex);
        }

        return new PageImpl<Item>(list, PageRequest.of(currentPage, pageSize), itemsList.size());
    }
}
