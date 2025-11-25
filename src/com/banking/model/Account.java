package com.banking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//serves as a template for diffrent account types
public abstract class Account {
    //fields common to all account types
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected LocalDateTime dateOpened;
    protected Customer customer; 
    protected List<Transaction> transactions;
    
    //constructor sets up the account and ensures account has a customer
    protected Account(String accountNumber, double initialBalance, String branch, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Account cannot exist without a customer");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.branch = branch;
        this.customer = customer;
        this.dateOpened = LocalDateTime.now();
        this.transactions = new ArrayList<>();
    }
    //adds mney to the acoount,record the transaction and returns true if succesful
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        recordTransaction("DEPOSIT", amount, "Deposit to account");
        return true;
    }
    //abstract method for withdrawing money, implemented by subclasses
    public abstract boolean withdraw(double amount);
    //abstract method for calculating interest, implemented by subclasses
    public abstract double calculateInterest();
    //applies calculated interest to the account balance and records the transaction
    public void applyInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            recordTransaction("INTEREST", interest, "Monthly interest applied");
        }
    }
    //records a transaction in the account's transaction history
    protected void recordTransaction(String type, double amount, String description) {
        Transaction transaction = new Transaction(
            generateTransactionId(),
            this.accountNumber,
            type,
            amount,
            balance,
            description,
            LocalDateTime.now()
        );
        transactions.add(transaction);
    }
    //generates a unique transaction id based on timestamp and account number
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + accountNumber.substring(0, 3);
    }
    //returns a copy of the account's transaction history
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions); 
    }
    //abstract method to get the account type, implemented by subclasses
    public abstract String getAccountType();
    
    public String getAccountNumber() {
        return accountNumber;
    }
    //getter and setter methods for account fields
    public double getBalance() {
        return balance;
    }
    
    protected void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public LocalDateTime getDateOpened() {
        return dateOpened;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    protected boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }
    //provides a string representation of the account
    @Override
    public String toString() {
        return String.format("%s[Number=%s, Balance=BWP %.2f, Customer=%s]",
            getAccountType(), accountNumber, balance, 
            customer.getFirstName() + " " + customer.getSurname());
    }
}
