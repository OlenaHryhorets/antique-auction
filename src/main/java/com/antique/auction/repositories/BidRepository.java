package com.antique.auction.repositories;

import com.antique.auction.models.Bid;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Integer> {
}
