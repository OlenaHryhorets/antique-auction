package com.antique.auction.repositories;

import com.antique.auction.models.Item;
import com.antique.auction.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByLogin(String login);

    List<User> findAllByItemsContains(Item item);

}
