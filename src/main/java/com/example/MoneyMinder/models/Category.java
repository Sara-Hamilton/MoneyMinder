package com.example.MoneyMinder.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    public Category(String name){
        this.name = name;
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

    public List<Transaction> getTransactions() { return transactions; }
}
