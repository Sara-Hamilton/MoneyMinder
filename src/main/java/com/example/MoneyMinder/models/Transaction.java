package com.example.MoneyMinder.models;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Transactional
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
    private Long id;

    @NotNull
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String description;

    @NotNull
    private Double amount;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "accountId")
    //@NotNull
    private Account account;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

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

    public Transaction(Double amount, Date date, Account account,
                       Category category, String description, User user){
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.category = category;
        this.description = description;
        this.user = user;
    }

    public Transaction(){ }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getAccount() { return account; }

    public void setAccount(Account account) {
        this.account = account;
    }


    /* public Account getFromAccount() {
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
    */

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    /*
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    //public List<Account> getAccounts() { return accounts; }
}
