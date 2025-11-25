package com.banking.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//represents a bank with customers and accounts
public class Bank {
    private String bankName;
    private String bankCode;
    
    private Map<String, Customer> customers; 
    private Map<String, Account> accounts; 
    private int customerCounter;
    private int accountCounter;
    //constructor initializes the bank with a name and code
    public Bank(String bankName, String bankCode) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.customerCounter = 1000; 
        this.accountCounter = 10000; }
    //registers a new customer in the bank
    public Customer registerCustomer(String firstName, String surname, String address) {
        String customerId = generateCustomerId();
        Customer customer = new Customer(customerId, firstName, surname, address);
        customers.put(customerId, customer);
        return customer;
    }
    //opens a new savings account for a customer
    public SavingsAccount openSavingsAccount(String customerId, double initialBalance, String branch) {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        String accountNumber = generateAccountNumber();
        SavingsAccount account = new SavingsAccount(accountNumber, initialBalance, branch, customer);
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        return account;
    }
    //opens a new investment account for a customer
    public InvestmentAccount openInvestmentAccount(String customerId, double initialBalance, String branch) {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        String accountNumber = generateAccountNumber();
        InvestmentAccount account = new InvestmentAccount(accountNumber, initialBalance, branch, customer);
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        return account;
    }
    //opens a new cheque account for a customer
    public ChequeAccount openChequeAccount(String customerId, double initialBalance, String branch,
                                           String companyName, String companyAddress) {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        String accountNumber = generateAccountNumber();
        ChequeAccount account = new ChequeAccount(accountNumber, initialBalance, branch, 
                                                   customer, companyName, companyAddress);
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        return account;
    }
    //processes monthly interest for all applicable accounts
    public void processMonthlyInterest() {
        for (Account account : accounts.values()) {
            if (account instanceof SavingsAccount || account instanceof InvestmentAccount) {
                account.applyInterest();
                System.out.println("Interest applied to account: " + account.getAccountNumber());
            }
        }
    }
    //retrieves a customer by their ID
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
    
    private String generateCustomerId() {
        return String.format("CUST-%04d", customerCounter++);
    }
     private String generateAccountNumber() {
        return String.format("%s-%05d", bankCode, accountCounter++);
    }
   
    public int getCustomerCount() {
        return customers.size();
    }
    public int getAccountCount() {
        return accounts.size();
    }
    
       public String getBankName() {
        return bankName;
    }
    
    public String getBankCode() {
        return bankCode;
    }
    //provides a string representation of the bank
    @Override
    public String toString() {
        return String.format("Bank[Name=%s, Code=%s, Customers=%d, Accounts=%d]",
            bankName, bankCode, customers.size(), accounts.size());
    }
}
