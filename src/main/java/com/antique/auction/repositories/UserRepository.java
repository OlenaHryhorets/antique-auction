package com.antique.auction.repositories;

import com.antique.auction.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByLogin(String login);
}
