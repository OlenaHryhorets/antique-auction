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

import java.util.*;

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
        if (registerNewUserAccount(user)) {
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

    @GetMapping(value = "/user/status/get/{login}", produces = {"application/json"})
    public @ResponseBody
    UserDTO getUserStatus(@PathVariable String login) {
        UserDTO userDTO = new UserDTO();
        User user = userRepository.findByLogin(login);
        List<ItemDTO> bidItems = new ArrayList<>();
        user.getItems().forEach(item -> {
            Optional<ItemDTO> dtoItemOptional = bidItems.stream().filter(itemDTO -> itemDTO.getId().equals(item.getId())).findFirst();
            dtoItemOptional.ifPresent(bidItems::remove);
            ItemDTO dtoItem = new ItemDTO();
            dtoItem.setName(item.getName());
            dtoItem.setId(item.getId());


            if (isUserCurrentBidderForItem(login, item)) {
                if (item.isAwarded()) {
                    dtoItem.setStatusName("WON");
                    Optional<ItemDTO> awardedDtoItemOptional = userDTO.getAwardedItems().stream().filter(itemDTO -> itemDTO.getId().equals(item.getId())).findFirst();
                    awardedDtoItemOptional.ifPresent(userDTO.getAwardedItems()::remove);
                    dtoItem.setFinalPrice(item.getCurrentPrice());
                    userDTO.getAwardedItems().add(dtoItem);
                } else {
                    dtoItem.setStatusName("IN_PROGRESS");
                }
            } else {
                dtoItem.setStatusName("LOST");
            }
            bidItems.add(dtoItem);
        });
        userDTO.getItems().addAll(bidItems);
        return userDTO;
    }

    private boolean isUserCurrentBidderForItem(@PathVariable String login, Item item) {
        return item.getBidUserLogin().equals(login);
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
