package com.antique.auction.services.impl;

import com.antique.auction.models.Item;
import com.antique.auction.repositories.ItemRepository;
import com.antique.auction.services.ItemService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String ORDERED_FIELD_NAME = "currentPrice";
    private static final String DEFAULT_ORDER_NAME = "default";
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> findPaginated(Pageable pageable, Optional<String> searchParam, Optional<String> sortOrder) {
        List<Item> itemsList = new ArrayList<>();
        Iterable<Item> itemIterable;
        if (searchParam.isPresent()) {
            if (sortOrder.isPresent() && isSortValueCorrect(sortOrder)) {
                itemIterable = itemRepository.findByNameContainingOrDescriptionContaining(searchParam.get(), searchParam.get(), Sort.by(Sort.Direction.valueOf(sortOrder.get()), ORDERED_FIELD_NAME));
            } else {
                itemIterable = itemRepository.findByNameContainingOrDescriptionContaining(searchParam.get(), searchParam.get(), Sort.unsorted());
            }
        } else {
            if (sortOrder.isPresent() && isSortValueCorrect(sortOrder)) {
                itemIterable = itemRepository.findAll(Sort.by(Sort.Direction.valueOf(sortOrder.get()), ORDERED_FIELD_NAME));
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

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), itemsList.size());
    }

    @Override
    public long count() {
        return itemRepository.count();
    }

    private boolean isSortValueCorrect(Optional<String> sortOrder) {
        return sortOrder.isPresent() && !sortOrder.get().isEmpty() && !sortOrder.get().equals(DEFAULT_ORDER_NAME);
    }

    @Override
    public Item findById(int id) {
        return itemRepository.findById(id).orElse(new Item());
    }

    @Override
    public void save(Item existingItem) {
        itemRepository.save(existingItem);
    }

    @Override
    public void deleteById(Integer id) {
        itemRepository.deleteById(id);
    }
}
