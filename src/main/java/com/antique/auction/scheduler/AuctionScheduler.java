package com.antique.auction.scheduler;

import com.antique.auction.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionScheduler {

    private final ItemService itemService;

    public AuctionScheduler(ItemService itemService) {
        this.itemService = itemService;
    }

    @Scheduled(cron = "${item-scheduler-value}")
    public void finalizeBids() {
        itemService.finalizeBids();
    }
}
