package com.example.MoneyMinder.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Transaction {

    //@ManyToOne
    //private Category category;

    // TODO fix this
    //@ManyToMany(mappedBy = "transactions")
    //@JoinColumn(name = "transaction_id")
    //private List<Account> accounts;

    @Id
    @GeneratedValue
    private int id;

    // TODO create private transactionType type;
    // create transactionType enum (deposit, withdrawal, transfer)

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull
    private User user;

    private String description;

    @NotNull
    private float amount;

    @NotNull
    private Date date;

    //@NotNull
    //private Account account;

    //@NotNull
    //private Category category;

    /*
    @NotNull
    private Account fromAccount;

    @NotNull
    private Account toAccount;


    @NotNull
    private Category fromCategory;

    @NotNull
    private Category toCategory;
    */

    public Transaction(float amount, Date date, Account account,
                       Category category, String description, User user){
        this.amount = amount;
        this.date = date;
        //this.account = account;
        //this.category = category;
        this.description = description;
        this.user = user;
    }

    public Transaction(){ }

    public int getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /*public Account getAccount() { return account; }

    public void setAccount(Account account) {
        this.account = account;
    }


    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }


    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }


    public Category getFromCategory() {
        return fromCategory;
    }

    public void setFromCategory(Category fromCategory) {
        this.fromCategory = fromCategory;
    }

    public Category getToCategory() {
        return toCategory;
    }

    public void setToCategory(Category toCategory) {
        this.toCategory = toCategory;
    }
    */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    //public List<Account> getAccounts() { return accounts; }
}
