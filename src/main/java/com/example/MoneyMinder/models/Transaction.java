package com.example.MoneyMinder.models;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Transactional
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String description;

    // @NotNull
    private BigDecimal amount;

    private BigDecimal previousTotal;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    private String categoryName;

    public Transaction(BigDecimal amount, Date date, Account account,
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(BigDecimal previousTotal) {
        this.previousTotal = previousTotal;
    }
}
