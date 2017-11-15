package com.example.MoneyMinder.models;

public enum TransactionType {

    DEPOSIT ("Deposit"),
    WITHDRAWAL ("Withdrawal"),
    TRANSFER ("Transfer");

    private final String name;

    TransactionType (String name) { this.name = name; }

    public String getname() { return name; }
}
