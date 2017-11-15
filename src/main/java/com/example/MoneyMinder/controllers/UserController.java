package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.User;
import com.example.MoneyMinder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("user")
@SessionAttributes("username")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("title", "Welcome test title");
        return "user/index";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Sign up for Money Minder");
        model.addAttribute(new User());
        return "user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid User newUser, Errors errors, Model model, String password,
                      String verifyPassword, HttpServletResponse response, HttpServletRequest request) {

        String username = newUser.getUsername();

        // TODO salt passwords
        //String salt = HashPass.saltShaker();
        //newUser.setSalt(salt);

        // username must be unique
        // check to see if username is already taken
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                model.addAttribute("title", "Try again");
                model.addAttribute(newUser);
                model.addAttribute("userErrorMessage", "That username is taken.");
                return "user/register";
            }
        }

        // check for errors
        // check that password and verify password match
        if (errors.hasErrors() || (!password.equals(verifyPassword))) {
            model.addAttribute("title", "Try again");
            model.addAttribute(newUser);
            if (!password.equals("") && !password.equals(verifyPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }
            return "user/register";
        } else {
            // TODO hash password
            //hashes password before saving to User
            //newUser.setPassword(HashPass.generateHash(salt + password));

            userDao.save(newUser);

            model.addAttribute("user", newUser);
            request.getSession().setAttribute("user", newUser);

            return "user/index";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpServletResponse response, HttpServletRequest request) {

        Iterable<User> users = userDao.findAll();

        for (User user : users) {

            // TODO salt and hash passwords
            // String salt = user.getSalt();
            // String enteredPassword = HashPass.generateHash(salt + password);

            // verify username and password match usernam and password pair in the database
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {

                // add user to session
                request.getSession().setAttribute("user", user);

                // save data to database
                userDao.save(user);

                model.addAttribute("user", user);
                model.addAttribute("title", "Welcome test title - change me later");

                return "/welcome";
            } else {
                model.addAttribute("title", "No user by that name or incorrect password!");
            }
        }
        return "user/index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request) {

        User aUser = (User) request.getSession().getAttribute("user");

        if (aUser == null) {
            model.addAttribute("title", "Welcome to Money Minder");
            return "redirect:/MoneyMinder";
        } else {
            model.addAttribute("title", "Click here to Logout.");
            return "user/logout";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpServletResponse response, HttpServletRequest request) {
        model.addAttribute("title", "You have successfully logged out");

        //Remove user from session
        request.getSession().removeAttribute("user");

        return "user/logout-confirmation";
    }
}
