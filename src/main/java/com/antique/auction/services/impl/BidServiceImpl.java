package com.antique.auction.services.impl;

import com.antique.auction.models.Bid;
import com.antique.auction.repositories.BidRepository;
import com.antique.auction.services.BidService;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;

    public BidServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public void save(Bid currentBid) {
        bidRepository.save(currentBid);
    }
}
