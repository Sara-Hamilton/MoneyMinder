package com.example.MoneyMinder.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
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
    //regex pattern prevents empty string but allows spaces within the string
    @Pattern(regexp="(.|\\s)*\\S(.|\\s)*", message="Name must not be empty.")
    private String name;

    // @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    List<Category> categories;

    private BigDecimal total;

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

    public BigDecimal getTotal() { return total; }

    public void setTotal(BigDecimal total) { this.total = total; }


    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    //public List<Transaction> transactions() { return transactions; }


    public List<Category> getCategories() {
        return categories;
    }

}
