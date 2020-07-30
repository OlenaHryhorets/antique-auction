package com.antique.auction.controllers;

import com.antique.auction.models.Item;
import com.antique.auction.models.User;
import com.antique.auction.models.dto.ItemDTO;
import com.antique.auction.models.dto.UserDTO;
import com.antique.auction.repositories.RoleRepository;
import com.antique.auction.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/registration")
    public ModelAndView goToRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(new User());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView addUser(User user) {
        ModelAndView modelAndView = new ModelAndView();
        if(registerNewUserAccount(user)) {
            modelAndView.addObject(user);
            modelAndView.addObject("registerSuccess", true);
            modelAndView.setViewName("login");
            return modelAndView;
        } else {
            modelAndView.addObject(user);
            modelAndView.addObject("registerError", true);
            modelAndView.setViewName("registration");
            return modelAndView;
        }
    }

    @GetMapping("/user-profile/view/{login}")
    public ModelAndView userProfileView(@PathVariable String login) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByLogin(login);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }

    @GetMapping(value = "/user/status/get/{login}", produces = { "application/json" })
    public @ResponseBody UserDTO getUserStatus(@PathVariable String login) {
        UserDTO userDTO = new UserDTO();
//        Item item = new Item();
//        item.setName("Item name");
//        userDTO.setItems(Arrays.asList(item));
        User user = userRepository.findByLogin(login);


        List<ItemDTO> biddedItems = new ArrayList<>();


// list of inProgress items:
        user.getItems().stream().filter(item -> !item.isAwarded() && item.getBidUserLogin().equals(login)).forEach(item1 -> {
            ItemDTO dtoItem = new ItemDTO();
            dtoItem.setName(item1.getName());
            dtoItem.setId(item1.getId());
            dtoItem.setStatusName("In progress");
            biddedItems.add(dtoItem);
        });

// list of awarded items:
        user.getItems().stream().filter(item -> item.isAwarded() && item.getBidUserLogin().equals(login)).forEach(item1 -> {
            ItemDTO dtoItem = new ItemDTO();
            dtoItem.setName(item1.getName());
            dtoItem.setId(item1.getId());
            dtoItem.setStatusName("Won");
            biddedItems.add(dtoItem);
            dtoItem.setFinalPrice(item1.getCurrentPrice());
            userDTO.getAwardedItems().add(dtoItem);
        });

// list of lost items:
        user.getItems().stream().filter(item -> !item.getBidUserLogin().equals(login)).forEach(item1 -> {
            ItemDTO dtoItem = new ItemDTO();
            dtoItem.setName(item1.getName());
            dtoItem.setId(item1.getId());
            dtoItem.setStatusName("Lost");
            biddedItems.add(dtoItem);
        });

        userDTO.getItems().addAll(biddedItems);



//        Item item = itemService.findById(itemId);
//        ItemDto itemDto = new ItemDto();
//        itemDto.setCurrentPrice(String.valueOf(item.getCurrentPrice()));
//        itemDto.setFinalPrice(String.valueOf(item.getCurrentPrice()));
//        List<User> users = item.getUsers();
//        if (users != null && !users.isEmpty()) {
//            itemDto.setFinalPriceUserName(users.get(users.size() - 1).getLogin());
//        }
        return userDTO;
    }

    private boolean registerNewUserAccount(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null || userRepository.findByLogin(user.getLogin()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        userRepository.save(user);
        return true;
    }
}
