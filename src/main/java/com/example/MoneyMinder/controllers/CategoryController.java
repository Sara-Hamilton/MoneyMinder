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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("title", user.getUsername() + "'s Categories");

        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model model,HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute(new Category());
        model.addAttribute("title", "Add Category");

        return "category/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, @ModelAttribute
    @Valid Category category, Errors errors, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        }

        category.setUser(user);
        categoryDao.save(category);
        userDao.save(user);
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        model.addAttribute("userCategories", userCategories);

        return "redirect:";
    }

    @RequestMapping(value = "edit/{categoryId}", method = RequestMethod.GET)
    public String displayCategoryEditForm(Model model, @PathVariable int categoryId, HttpServletRequest request) {

        Category category = categoryDao.findOne(categoryId);
        User user = (User) request.getSession().getAttribute("user");

        // only categories that have not been transacted against may be deleted
        boolean usedCategory = false;
        List<Transaction> transactions = transactionDao.findByUserId(user.getId());
        for ( Transaction transaction : transactions) { if (transaction.getCategory() == category) {
            usedCategory = true; }
        }

        model.addAttribute("category", category);
        model.addAttribute("usedCategory", usedCategory);
        model.addAttribute("title", "Edit Category " + category.getName());

        return "category/edit";
    }

    @RequestMapping(value = "edit", method = {RequestMethod.POST})
    public String processCategoryEditForm(Model model, @ModelAttribute @Valid Category category, Errors errors,
                                         @RequestParam int categoryId, String name) {

        if(errors.hasErrors()) {
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("title", "Errors " + category.getName());
            return "category/edit";
        }

        categoryDao.findOne(categoryId).setName(name);
        categoryDao.save(categoryDao.findOne(categoryId));

        return "redirect:";
    }

    @RequestMapping(value = "remove/{categoryId}", method = RequestMethod.GET)
    public String displayRemoveCategoryForm(Model model, @PathVariable int categoryId) {

        model.addAttribute("category", categoryDao.findOne(categoryId));
        model.addAttribute("title", "Delete Category");

        return "category/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCategoryForm(@RequestParam int categoryId) {

        Category category = categoryDao.findOne(categoryId);
        categoryDao.delete(category);

        return "redirect:";
    }

    // ability to view transactions by category
    @RequestMapping(value = "transactions", method = RequestMethod.GET)
    public String displayCategoryTransactionsForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());

        model.addAttribute("title", "Display Transactions By Category");
        model.addAttribute("userCategories", userCategories);

        return "category/view";
    }

    @RequestMapping(value = "transactions", method = RequestMethod.POST)
    public String diaplayCategoryTransactions(Model model, @RequestParam int categoryId, @DateTimeFormat(pattern = "yyyy-MM-dd")Date fromDate, @DateTimeFormat(pattern = "yyyy-MM-dd")Date toDate, HttpServletRequest request) {

        Category category = categoryDao.findOne(categoryId);
        User user = (User) request.getSession().getAttribute("user");

        List<Transaction> userTransactions = transactionDao.findByUserId(user.getId());
        List<Transaction> categoryTransactions = new ArrayList<>();
        for ( Transaction transaction : userTransactions) { 
            if ((transaction.getCategory() == category) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                    categoryTransactions.add(transaction); }
            }

        BigDecimal sum = new BigDecimal(0);
        for (Transaction transaction : categoryTransactions){
            if (transaction.getType() == TransactionType.DEPOSIT){
                sum = sum.add(new BigDecimal(String.valueOf(transaction.getAmount())));}
            else if (transaction.getType() == TransactionType.WITHDRAWAL){
                sum = sum.subtract(new BigDecimal(String.valueOf(transaction.getAmount())));
            }
        }

        model.addAttribute("title", "Transactions In Category " + category.getName());
        model.addAttribute("categoryTransactions", categoryTransactions);
        model.addAttribute("sum", sum);

        return "category/transactions";
    }

    // ability to view transactions by category and account
    @RequestMapping(value = "transactions-account", method = RequestMethod.GET)
    public String displayCategoryAndAccountTransactionsForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        List<Account> userAccounts = accountDao.findByUserId(user.getId());

        model.addAttribute("title", "Display Transactions By Account and Category");
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("userAccounts", userAccounts);

        return "category/view-account";
    }

    @RequestMapping(value = "transactions-account", method = RequestMethod.POST)
    public String diaplayCategoryAndAccountTransactions(Model model, int categoryId, int accountId, @DateTimeFormat(pattern = "yyyy-MM-dd")Date fromDate, @DateTimeFormat(pattern = "yyyy-MM-dd")Date toDate, HttpServletRequest request) {

        if (categoryId != 0 ){Category category = categoryDao.findOne(categoryId);}
        if (categoryId == 0){ String category = "All Categories";}
        Account account = accountDao.findOne(accountId);
        User user = (User) request.getSession().getAttribute("user");

        List<Transaction> userTransactions = transactionDao.findByUserId(user.getId());
        List<Transaction> categoryTransactions = new ArrayList<>();
        for ( Transaction transaction : userTransactions) {
            if (categoryId != 0) {
                if ((transaction.getCategory() == categoryDao.findOne(categoryId)) && (transaction.getAccount() == account) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                categoryTransactions.add(transaction); }
            }
            else if ((transaction.getAccount() == account) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                    categoryTransactions.add(transaction);
            }
        }

        BigDecimal sum = new BigDecimal(0);
        for (Transaction transaction : categoryTransactions){
            if (transaction.getType() == TransactionType.DEPOSIT){
                sum = sum.add(new BigDecimal(String.valueOf(transaction.getAmount())));}
            else if (transaction.getType() == TransactionType.WITHDRAWAL){
                sum = sum.subtract(new BigDecimal(String.valueOf(transaction.getAmount())));
            }
        }

        model.addAttribute("title", "Transactions In Account " + account.getName());
        model.addAttribute("categoryTransactions", categoryTransactions);
        model.addAttribute("sum", sum);

        return "category/transactions-account";
    }
}
