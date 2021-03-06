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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
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
            List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
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

        BigDecimal transactionAmount = transaction.getAmount();
        BigDecimal total = account.getTotal();
        BigDecimal userTotal = user.getUserTotal();

        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);
        if(categoryId != 0) {transaction.setCategoryName(category.getName());}
        transaction.setPreviousTotal(total);
        transactionDao.save(transaction);

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

    @RequestMapping(value = "transfer", method = RequestMethod.GET)
    public String displayTransferForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        List<Account> userAccounts = accountDao.findByUserId(user.getId());

        model.addAttribute("userAccounts", userAccounts);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute(new Transaction());
        model.addAttribute("title", "Transfer Between Accounts");

        return "transaction/transfer";
    }

    @RequestMapping(value="transfer", method = RequestMethod.POST)
    public String processTransferForm(Model model, @ModelAttribute @Valid Transaction transaction,
                                            Errors errors, @RequestParam int categoryId, @RequestParam int fromAccountId,
                                            @RequestParam int toAccountId, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        if (errors.hasErrors()) {
            List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
            List<Account> userAccounts = accountDao.findByUserId(user.getId());
            model.addAttribute("userAccounts", userAccounts);
            model.addAttribute("userCategories", userCategories);
            model.addAttribute(new Transaction());
            model.addAttribute("title", "Errors");
            return "transaction/transfer";
        }

        Category category = categoryDao.findOne(categoryId);
        Account fromAccount = accountDao.findOne(fromAccountId);
        Account toAccount = accountDao.findOne(toAccountId);

        Date date = transaction.getDate();
        String description = transaction.getDescription();

        BigDecimal transactionAmount = transaction.getAmount();
        BigDecimal fromTotal = fromAccount.getTotal();
        BigDecimal toTotal = toAccount.getTotal();

        // withdrawal from fromAccount
        BigDecimal newFromTotal = fromTotal.subtract(transactionAmount);
        fromAccount.setTotal(newFromTotal);

        // save withdrawal transaction
        transaction.setUser(user);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAccount(fromAccount);
        transaction.setCategory(category);
        if(categoryId != 0) {transaction.setCategoryName(category.getName());}
        transaction.setPreviousTotal(fromTotal);
        transactionDao.save(transaction);
        accountDao.save(fromAccount);

        //create a new transaction for deposit into toAccount
        Transaction toTransaction = new Transaction();

        // deposit into toAccount
        BigDecimal newToTotal = toTotal.add(transactionAmount);
        toAccount.setTotal(newToTotal);

        // save deposit transaction
        toTransaction.setAmount(transactionAmount);
        toTransaction.setDescription(description);
        toTransaction.setDate(date);
        toTransaction.setUser(user);
        toTransaction.setType(TransactionType.DEPOSIT);
        toTransaction.setAccount(toAccount);
        toTransaction.setCategory(category);
        if(categoryId != 0) {toTransaction.setCategoryName(category.getName());}
        toTransaction.setPreviousTotal(toTotal);
        transactionDao.save(toTransaction);
        accountDao.save(toAccount);

        model.addAttribute("title", "Transaction Successful");
        return "transaction/transaction-confirmation";
    }

    // ability to view transactions by category, account, and date range
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String displayCategoryAndAccountTransactionsForm(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserIdOrderByNameAsc(user.getId());
        List<Account> userAccounts = accountDao.findByUserId(user.getId());

        model.addAttribute("title", "View Transactions By Account and Category");
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("userAccounts", userAccounts);

        return "transaction/filter";
    }

    @RequestMapping(value = "view", method = RequestMethod.POST)
    public String diaplayCategoryAndAccountTransactions(Model model, int categoryId, int accountId, @DateTimeFormat(pattern = "yyyy-MM-dd")Date fromDate, @DateTimeFormat(pattern = "yyyy-MM-dd")Date toDate, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        List<Transaction> userTransactions = transactionDao.findByUserId(user.getId());
        List<Transaction> filteredTransactions = new ArrayList<>();
        for ( Transaction transaction : userTransactions) {
            if ((categoryId != 0) && (accountId != 0)) {
                if ((transaction.getCategory() == categoryDao.findOne(categoryId)) && (transaction.getAccount() == accountDao.findOne(accountId)) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                    filteredTransactions.add(transaction); }
            }
            else if ((categoryId != 0) && (accountId == 0)){
                    if ((transaction.getCategory() == categoryDao.findOne(categoryId)) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                        filteredTransactions.add(transaction);}
            }
            else if ((categoryId == 0) && (accountId != 0)) {
                if ((transaction.getAccount() == accountDao.findOne(accountId)) && (!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                    filteredTransactions.add(transaction); }
            }
            else if ((categoryId == 0) && (accountId == 0)) {
                if ((!transaction.getDate().before(fromDate)) && (!transaction.getDate().after(toDate))) {
                    filteredTransactions.add(transaction); }
            }
        }

        BigDecimal sum = new BigDecimal(0);
        for (Transaction transaction : filteredTransactions){
            if (transaction.getType() == TransactionType.DEPOSIT){
                sum = sum.add(new BigDecimal(String.valueOf(transaction.getAmount())));}
            else if (transaction.getType() == TransactionType.WITHDRAWAL){
                sum = sum.subtract(new BigDecimal(String.valueOf(transaction.getAmount())));
            }
        }

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String FromDate = df.format(fromDate);
        String ToDate = df.format(toDate);

        model.addAttribute("title", "Transactions Between " + FromDate + " and " + ToDate);
        model.addAttribute("filteredTransactions", filteredTransactions);
        model.addAttribute("sum", sum);

        return "transaction/view";
    }
}
