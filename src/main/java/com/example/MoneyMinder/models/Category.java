package com.example.MoneyMinder.models;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Entity
public class Category {

    @OneToMany
   // @JoinColumn(name = "category_id")
    private List<Transaction> transactions = new ArrayList<>();

    @Id
    @GeneratedValue
    public int id;

    @NotNull
    @Size(min=2, max=45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category(String name, User user){
        this.name = name;
        this.user = user;
    }

    public Category() { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public List<Transaction> getTransactions() { return transactions; }
}
