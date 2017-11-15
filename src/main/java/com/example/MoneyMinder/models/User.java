package com.example.MoneyMinder.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class User {

    @OneToMany
    private List<Account> accounts;

    //@OneToMany
    //private List<Transaction> transactions;

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String username;

    @NotNull
    @Size(min=3, max=50)
    private String email;

    @NotNull
    @Size(min=6, max=40)
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {}

    /*@Id
    @GeneratedValue
    private int userId;
    private static int nextId = 1;

    public User() {
        userId = nextId;
        nextId++;
    } */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() { return id; }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /*
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    */
}
