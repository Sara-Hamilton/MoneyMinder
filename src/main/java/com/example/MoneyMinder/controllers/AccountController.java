package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.Account;
import com.example.MoneyMinder.models.User;
import com.example.MoneyMinder.models.data.AccountDao;
import com.example.MoneyMinder.models.data.CategoryDao;
import com.example.MoneyMinder.models.data.TransactionDao;
import com.example.MoneyMinder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("user", user);
        model.addAttribute("title", "Welcome test title for account index");
        return "account/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddAccountForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("title", "Create New Account");
        model.addAttribute(new Account());

        return "account/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddAccountForm(Model model, @ModelAttribute @Valid Account account, Errors errors, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create New Account");
            return "account/add";
        }

        account.setUser(user);
        account.setTotal(BigDecimal.valueOf(0.00));
        accountDao.save(account);
        userDao.save(user);
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        model.addAttribute("userAccounts", userAccounts);
        return "account/index";
    }

    @RequestMapping(value = "view/{accountId}", method = RequestMethod.GET)
    public String viewAccount(Model model, @PathVariable int accountId) {

        model.addAttribute("account", accountDao.findOne(accountId));
        model.addAttribute("transactionList", transactionDao.findByAccountIdOrderByIdDesc(accountId));
        model.addAttribute("categories", categoryDao.findAll());

        return "account/view";
    }

    @RequestMapping(value = "edit/{accountId}", method = RequestMethod.GET)
    public String displayAccountEditForm(Model model, @PathVariable int accountId) {

        Account account = accountDao.findOne(accountId);

        model.addAttribute("account", account);
        model.addAttribute("title", "Edit Account " + account.getName());

        return "account/edit";
    }

    @RequestMapping(value = "edit", method = {RequestMethod.POST})
    public String processAccountEditForm(Model model, @ModelAttribute @Valid Account account, Errors errors,
                                         @RequestParam int accountId, String name, BigDecimal minimum, BigDecimal goal) {

        if(errors.hasErrors()) {
            model.addAttribute("accountId", accountId);
            model.addAttribute("title", "Edit Account " + account.getName());
            return "account/edit";
        }

        accountDao.findOne(accountId).setName(name);
        accountDao.findOne(accountId).setMinimum(minimum);
        accountDao.findOne(accountId).setGoal(goal);

        return "redirect:";
    }

    @RequestMapping(value = "remove/{accountId}", method = RequestMethod.GET)
    public String displayRemoveAccountForm(Model model, @PathVariable int accountId) {

        Account account = accountDao.findOne(accountId);

        model.addAttribute("account", account);
        model.addAttribute("title", "Delete Account " + account.getName());

        return "account/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveAccountForm(Model model, @ModelAttribute @Valid Account account, Errors errors, @RequestParam int accountId,
                                           HttpServletRequest request) {

        Account acct = accountDao.findOne(accountId);
        BigDecimal x = BigDecimal.valueOf(0.00);

        if(x.compareTo(acct.getTotal()) != 0) {
            model.addAttribute("account", acct);
            model.addAttribute("remainingBalanceMessage", "Only accounts with a balance of $0.00 can be deleted.");

            return "account/remove";
        } else { accountDao.delete(acct);

            User user = (User) request.getSession().getAttribute("user");
            List<Account> userAccounts = accountDao.findByUserId(user.getId());
            model.addAttribute("userAccounts", userAccounts);

        return "account/index"; }
        // return "redirect:";
    }
}
