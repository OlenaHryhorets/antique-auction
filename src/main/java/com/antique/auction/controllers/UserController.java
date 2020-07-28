package com.antique.auction.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @GetMapping(value = "/registration")
    public String goToRegistrationForm() {
        return "registration";
    }

    @PostMapping(value = "/registration")
    public ModelAndView addUser (){
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("login");
    return modelAndView;
}
}
