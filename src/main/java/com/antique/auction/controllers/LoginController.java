package com.antique.auction.controllers;

import com.antique.auction.models.Bid;
import com.antique.auction.models.Item;
import com.antique.auction.models.Role;
import com.antique.auction.models.User;
import com.antique.auction.repositories.RoleRepository;
import com.antique.auction.repositories.UserRepository;
import com.antique.auction.services.BidService;
import com.antique.auction.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class LoginController {
    private final ItemService itemService;
    private final BidService bidService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(ItemService itemsService, BidService bidService, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserRepository userRepository) {
        this.itemService = itemsService;
        this.bidService = bidService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {
        addUsersAndRolesIfNeeded();
        if (itemService.count() == 0) {
            addInitialDemoData();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    private void addUsersAndRolesIfNeeded() {
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role userRole = createRoleIfNotFound("USER");
        User admin = userRepository.findByLogin("admin");
        if (admin == null) {
            admin = new User();
            admin.setLogin("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@test.com");
            admin.setEnabled(true);
            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);
        }
        User user = userRepository.findByLogin("user");
        if (user == null) {
            user = new User();
            user.setLogin("user");
            user.setFirstName("user");
            user.setLastName("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@test.com");
            user.setRoles(Collections.singletonList(userRole));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Transactional
    Role createRoleIfNotFound(
            String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

    private void addInitialDemoData() {
        saveItem("demoItem1", "demo description 1", 10);
        saveItem("demoItem2", "demo description 21", 20);
        saveItem("demoItem3", "demo description 31", 100);
        saveItem("demoItem4", "demo description 41", 15);
        saveItem("demoItem5", "demo description 51", 10);
        saveItem("demoItem6", "demo description 61", 35);
        saveItem("demoItem7", "demo description 71", 11);
        saveItem("demoItem8", "demo description 81", 30);
        saveItem("demoItem9", "demo description 91", 10);
        saveItem("demoItem10", "demo description 101", 10);
        saveItem("demoItem11", "demo description 102", 55);
    }

    private Item saveItem(String name, String description, int price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCurrentPrice(price);
        item.setCurrentBidDate(LocalDateTime.now());
        User currentUser = userRepository.findByLogin("user");
        item.setBidUserLogin(currentUser.getLogin());
        item.setImageName(name + ".png");
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        item.setDateString(LocalDateTime.now().plusHours(20).format(customFormatter));
        Bid bid = new Bid();
        bid.setPriceValue(item.getCurrentPrice());
        bid.setItem(item);
        bid.setBidDate(LocalDateTime.now());
        itemService.save(item);
        currentUser.addItem(item);
        bidService.save(bid);
        return item;
    }
}
