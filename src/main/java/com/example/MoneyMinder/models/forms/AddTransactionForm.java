package com.example.MoneyMinder.models.forms;

import com.example.MoneyMinder.models.Account;
import com.example.MoneyMinder.models.Transaction;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class AddTransactionForm {

    private Account account;

    private Iterable<Transaction> transactions;

    @Id
    @GeneratedValue
    @NotNull
    private int accountId;

    @Id
    @GeneratedValue
    @NotNull
    private int transactionId;

    public AddTransactionForm() {
    }

    public AddTransactionForm(Account account, Iterable<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Iterable<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Iterable<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
