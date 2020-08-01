package com.antique.auction.services.impl;

import com.antique.auction.email.EmailService;
import com.antique.auction.models.Item;
import com.antique.auction.models.User;
import com.antique.auction.repositories.ItemRepository;
import com.antique.auction.repositories.UserRepository;
import com.antique.auction.services.ItemService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String ORDERED_FIELD_NAME = "currentPrice";
    private static final String DEFAULT_ORDER_NAME = "default";
    private final ItemRepository itemRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, EmailService emailService, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
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

    @Override
    public void finalizeBids() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Item> items = itemRepository.findAllByDateStringIsNotNullAndCurrentPriceIsNotNullAndAwardedFalse();
        items.forEach(item -> {
            LocalDateTime itemDate = LocalDateTime.parse(item.getDateString(), formatter);
            if (itemDate.isBefore(LocalDateTime.now()) || itemDate.isEqual(LocalDateTime.now())) {
                item.setAwarded(true);
                itemRepository.save(item);
                User bidUser = userRepository.findByLogin(item.getBidUserLogin());

                //send email to bid user
                Map<String, Object> params = new HashMap<>();
                params.put("itemName", item.getName());
                params.put("bid", item.getCurrentPrice());
                params.put("bidDate", item.getDateString());
                try {
                    if (bidUser != null) {
                        emailService.sendAwardedEmail(bidUser.getEmail(), "You won the item!", params);
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                item.getUsers().stream().filter(user -> !user.equals(bidUser)).forEach(user -> {
                    Map<String, Object> bidParams = new HashMap<>();
                    bidParams.put("itemName", item.getName());
                    bidParams.put("bid", item.getCurrentPrice());
                    bidParams.put("bidDate", item.getDateString());
                    try {
                        emailService.sendFinishBidEmail(user.getEmail(), "Biding has finished", bidParams);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
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
