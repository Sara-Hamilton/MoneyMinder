package com.example.MoneyMinder.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Account {

    //@ManyToMany
    //@JoinColumn(name = "account_id")
    //private List<Transaction> transactions;


    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=2, max=45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    List<Category> categories;

    private float total;

    public Account(User user, String name){
        // this();
        this.user = user;
        this.name = name;
        //this.categories = categories;
    }

    public Account(){ }

    /*
    public void addItem(Transaction item) {transactions.add(item);}

    public void removeItem(Transaction item) {transactions.remove(item);}
    */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getTotal() { return total; }

    public void setTotal(float total) { this.total = total; }

    //public List<Transaction> transactions() { return transactions; }

    /*
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    */
}
