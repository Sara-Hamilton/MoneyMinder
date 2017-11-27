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
import java.math.BigDecimal;
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
        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("types", TransactionType.values());
        model.addAttribute(new Transaction());
        model.addAttribute("title", "Add Transaction");

        return "transaction/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddTransactionForm(Model model, @ModelAttribute @Valid Transaction transaction,
                                            Errors errors, @RequestParam int categoryId, @RequestParam int accountId,
                                            HttpServletRequest request) {
  
        User user = (User) request.getSession().getAttribute("user");

        if (errors.hasErrors()) {
            List<Category> userCategories = categoryDao.findByUserId(user.getId());
            List<Account> userAccounts = accountDao.findByUserId(user.getId());
            model.addAttribute("userAccounts", userAccounts);
            model.addAttribute("userCategories", userCategories);
            model.addAttribute("types", TransactionType.values());
            model.addAttribute(new Transaction());
            model.addAttribute("title", "Errors");
            return "transaction/add";
        }

        Category category = categoryDao.findOne(categoryId);
        Account account = accountDao.findOne(accountId);
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setCategoryName(category.getName());
        transactionDao.save(transaction);

        BigDecimal transactionAmount = transaction.getAmount();
        BigDecimal total = account.getTotal();
        BigDecimal userTotal = user.getUserTotal();

        if (transaction.getType() == TransactionType.DEPOSIT) {
            BigDecimal newTotal = total.add(transactionAmount);
            account.setTotal(newTotal);
            BigDecimal newUserTotal = userTotal.add(transactionAmount);
            user.setUserTotal(newUserTotal);
        } else if (transaction.getType() == TransactionType.WITHDRAWAL) {
            BigDecimal newTotal = total.subtract(transactionAmount);
            account.setTotal(newTotal);
            BigDecimal newUserTotal = userTotal.subtract(transactionAmount);
            user.setUserTotal(newUserTotal);
        }

        accountDao.save(account);
        userDao.save(user);

        model.addAttribute("title", "Transaction Successful");
        return "transaction/transaction-confirmation";
    }
}
