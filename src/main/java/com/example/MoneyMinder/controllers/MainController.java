package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("MoneyMinder")
public class MainController {
    @RequestMapping(value = "")
    public String index(Model model,HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "Welcome test title");
        return "/welcome";
    }
}
