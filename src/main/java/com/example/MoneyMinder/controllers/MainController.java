package com.example.MoneyMinder.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("MoneyMinder")
public class MainController {
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("title", "Welcome test title");
        return "/welcome";
    }
}
