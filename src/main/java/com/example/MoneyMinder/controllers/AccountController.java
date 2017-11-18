package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.Account;
import com.example.MoneyMinder.models.Transaction;
import com.example.MoneyMinder.models.User;
import com.example.MoneyMinder.models.data.AccountDao;
import com.example.MoneyMinder.models.data.TransactionDao;
import com.example.MoneyMinder.models.data.UserDao;
import com.example.MoneyMinder.models.forms.AddTransactionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
        // model.addAttribute("accounts", accountDao.findAll());

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

        // TODO work on this once transactions are functioning
        Account account = accountDao.findOne(accountId);
        model.addAttribute("account", account);

        return "account/view";
    }

    @RequestMapping(value = "add-transaction/{accountId}", method = RequestMethod.GET)
    public String addTransaction(Model model, @PathVariable int accountId) {

        // TODO work on this once transactions are functioning
        Account account = accountDao.findOne(accountId);
        AddTransactionForm form = new AddTransactionForm(account, transactionDao.findAll());

        model.addAttribute("form", form);
        model.addAttribute("title", "Add Transaction to Account: " + account.getName());

        return "menu/add-item";
    }

    public String processAddTransactionForm(@ModelAttribute @Valid AddTransactionForm form, Errors errors,
                                            @RequestParam int accountId, @RequestParam int transactionId, Model model) {

        // TODO work on this once transactions are functioning
        Account account = accountDao.findOne(accountId);
        Transaction transaction = transactionDao.findOne(transactionId);
        form = new AddTransactionForm(account, transactionDao.findAll());
        // TODO implement cheeseOnMenu check if needed

        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("form", form);
            model.addAttribute("title", "Add Transaction to Account: " + account.getName());
            return "account/add-item";
        } else {
            // TODO account.addItem(transaction);
            accountDao.save(account);
            return "redirect:view/" + accountId;
        }
    }

}
