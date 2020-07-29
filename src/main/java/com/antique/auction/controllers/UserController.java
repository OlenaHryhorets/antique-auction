package com.antique.auction.controllers;

import com.antique.auction.models.User;
import com.antique.auction.repositories.RoleRepository;
import com.antique.auction.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;

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

    private boolean registerNewUserAccount(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null | userRepository.findByLogin(user.getLogin()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        userRepository.save(user);
        return true;
    }
}
