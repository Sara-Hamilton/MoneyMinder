package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.*;
import com.example.MoneyMinder.models.data.AccountDao;
import com.example.MoneyMinder.models.data.CategoryDao;
import com.example.MoneyMinder.models.data.TransactionDao;
import com.example.MoneyMinder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionDao transactionDao;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddTransactionForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserId(user.getId());
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("types", TransactionType.values());
        model.addAttribute(new Transaction());
        model.addAttribute("title", "Add Transaction");

        return "transaction/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddTransactionForm(Model model, @RequestParam (required = false) int categoryId, @RequestParam (required = false) int
            accountId, @RequestParam TransactionType type, @RequestParam Double amount, @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                            @RequestParam (required = false) String description, HttpServletRequest request) {

        // TODO configure transaction logic
        
        User user = (User) request.getSession().getAttribute("user");
        //model.addAttribute("user", user);
        Transaction transaction = new Transaction();
        Category category = categoryDao.findOne(categoryId);
        Account account = accountDao.findOne(accountId);
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setDate(date);
        transactionDao.save(transaction);
        userDao.save(user);

        model.addAttribute("title", "Transaction Complete");
        return "transaction/transaction-confirmation";
    }
}
