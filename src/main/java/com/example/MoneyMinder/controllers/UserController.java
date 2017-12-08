package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.Account;
import com.example.MoneyMinder.models.User;
import com.example.MoneyMinder.models.converters.HashPass;
import com.example.MoneyMinder.models.data.AccountDao;
import com.example.MoneyMinder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("user")
//@SessionAttributes("username")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        // TODO change this - either get rid of it or have it direct to some sort of user page

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
        String salt = HashPass.saltShaker();
        newUser.setSalt(salt);

        // username must be unique
        // check to see if username is already taken
        Iterable<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                model.addAttribute("title", "Sign up for Money Minder");
                model.addAttribute(newUser);
                model.addAttribute("userErrorMessage", "That username is taken.");
                return "user/register";
            }
        }

        // check for errors
        // check that password and verify password match
        if (errors.hasErrors() || (!password.equals(verifyPassword))) {
            model.addAttribute("title", "Sign up for Money Minder");
            model.addAttribute(newUser);
            if (!password.equals("") && !password.equals(verifyPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
            }
            return "user/register";
        } else {
            //hashes password before saving to User
            newUser.setPassword(HashPass.generateHash(salt + password));

            newUser.setUserTotal(BigDecimal.valueOf(0.00));
            userDao.save(newUser);

            model.addAttribute("user", newUser);
            request.getSession().setAttribute("user", newUser);
            List<Account> userAccounts = accountDao.findByUserId(newUser.getId());
            model.addAttribute("userAccounts", userAccounts);
            model.addAttribute("title", "Welcome test title - change me later");

            return "account/index";
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
            
            String salt = user.getSalt();
            String enteredPassword = HashPass.generateHash(salt + password);

            // verify username and password match username and password pair in the database
            if (user.getUsername().equals(username) && user.getPassword().equals(enteredPassword)) {
                // add user to session
                request.getSession().setAttribute("user", user);
                // save data to database
                userDao.save(user);

                List<Account> userAccounts = accountDao.findByUserId(user.getId());

                model.addAttribute("userAccounts", userAccounts);
                model.addAttribute("user", user);
                model.addAttribute("title", "Welcome test title - change me later");
                return "account/index";

        } else {
                int userExists = 0;
                for (User user1 : users)
                    if ( user1.getUsername().equals(username)){
                        userExists += 1;
                    }
                if (userExists == 0){
                    model.addAttribute("title", "Login");
                    model.addAttribute("userErrorMessage", "Invalid Username");
                    return "user/login";
                }
            }
            model.addAttribute("title", "Login");
            model.addAttribute("username", username);
            model.addAttribute("passwordErrorMessage", "Invalid Password");
        }
        return "user/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {

            model.addAttribute("title", "Click here to Logout");
            return "user/logout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpServletResponse response, HttpServletRequest request) {
        model.addAttribute("title", "You have successfully logged out");

        //Remove user from session
        request.getSession().removeAttribute("user");

        return "user/logout-confirmation";
    }

    @RequestMapping(value = "/hideMinAndGoal", method = RequestMethod.GET)
    public String hideMinAndGoal(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        boolean currentMinAndGoal = user.isHideMinAndGoal();
        if (currentMinAndGoal){
            user.setHideMinAndGoal(false);
        } else {user.setHideMinAndGoal(true);
        }

        userDao.save(user);

        model.addAttribute("user", user);
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("user", user);
        model.addAttribute("hideMinAndGoal", user.isHideMinAndGoal());
        model.addAttribute("title", "Welcome test title - change me later");

        return "account/index";
    }
}
