package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.*;
import com.example.MoneyMinder.models.data.AccountDao;
import com.example.MoneyMinder.models.data.CategoryDao;
import com.example.MoneyMinder.models.data.TransactionDao;
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
import javax.validation.Valid;
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
        //List<Category> userCategories = user.getCategories();
        List<Account> userAccounts = accountDao.findByUserId(user.getId());
        //model.addAttribute("user", user);
        model.addAttribute("user.accounts", user.getAccounts());
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("types", TransactionType.values());
        model.addAttribute(new Transaction());
        model.addAttribute("title", "Add Transaction");

        return "transaction/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddTransactionForm(Model model, @ModelAttribute
    @Valid Transaction transaction, Errors errors, @RequestParam (required = false) Long categoryId, @RequestParam (required = false) Long
            accountId, HttpServletRequest request) {

        // @RequestParam TransactionType type, @RequestParam BigDecimal amount, @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
        //@RequestParam (required = false) String description,

        //int acctId = accountId.intValue();

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        if (errors.hasErrors()) {
            List<Category> userCategories = categoryDao.findByUserId(user.getId());
            List<Account> userAccounts = accountDao.findByUserId(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userAccounts", userAccounts);
            model.addAttribute("userCategories", userCategories);
            model.addAttribute("types", TransactionType.values());
            model.addAttribute(new Transaction());
            model.addAttribute("title", "Errors");
            return "transaction/add";
        }

        //Category category = categoryDao.findOne(Math.toIntExact(categoryId));
        //Account account = accountDao.findOne(Math.toIntExact(accountId));

        //Account account = transaction.getAccount();
        //transaction.setUser(transaction.getUser());
        //transaction.setAccount(account);
        //transaction.setCategory(category);
        transactionDao.save(transaction);
        userDao.save(user);
        //List<Category> userCategories = categoryDao.findByUserId(user.getId());
        //model.addAttribute("userCategories", userCategories);

        model.addAttribute("title", "Transaction Successful");
        return "transaction/transaction-confirmation";
    }
}
