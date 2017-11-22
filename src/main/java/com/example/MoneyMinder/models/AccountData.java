package com.example.MoneyMinder.models;

import java.util.ArrayList;

public class AccountData {

    static ArrayList<Account> accounts = new ArrayList<>();

    //add
    public static void add(Account newAccount) { accounts.add(newAccount); }

    //getAll
    public static ArrayList<Account> getAll() { return accounts; }

    //getById
    public static Account getById(int id) {
        Account anAccount = null;

        for (Account someAccount : accounts) {
            if (someAccount.getId() == id) {
                anAccount = someAccount;
            }
        }
        return anAccount;
    }

    //getTotal
    //public static BigDecimal userGrandTotal(int id) {}

    //getByUserId
    /*public static Account getByUserId(int userId) {
        Account anAccount = null;

        List<Account> userAccounts = null;

        for (Account someAccount : accounts) {
            if (someAccount.getUserId() == userId) {
                anAccount.put(userAccounts);
            }
        }
        return userAccounts;
    } */
}
