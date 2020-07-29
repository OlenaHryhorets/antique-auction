package com.antique.auction.repositories;

import com.antique.auction.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Privilege findByName(String privilegeName);
}
