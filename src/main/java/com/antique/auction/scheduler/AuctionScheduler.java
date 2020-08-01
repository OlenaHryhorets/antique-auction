package com.antique.auction.scheduler;

import com.antique.auction.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionScheduler {
    Logger logger = LoggerFactory.getLogger(AuctionScheduler.class);

    private final ItemService itemService;

    public AuctionScheduler(ItemService itemService) {
        this.itemService = itemService;
    }

    @Scheduled(cron = "${item-scheduler-value}")
    public void finalizeBids() {
        logger.info("Scheduler worked");
        itemService.finalizeBids();
    }
}
