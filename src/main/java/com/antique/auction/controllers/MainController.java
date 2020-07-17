package com.antique.auction.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    public String home () {
        return "home";
    }
}
