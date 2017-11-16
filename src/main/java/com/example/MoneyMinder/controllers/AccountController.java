package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("account")
public class AccountController {

    /*@RequestMapping(value="")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

    @RequestMapping(value="goodbye")
    @ResponseBody
    public String goodbye() {
        return "Goodbye!";
    } */

    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("title", "Welcome test title for account index");
        model.addAttribute("user", user);
        return "account/index";
    }


}
