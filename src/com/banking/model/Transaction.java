package com.banking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final String transactionType; 
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final LocalDateTime timestamp;
	//constructor initializes all transaction details
    public Transaction(String transactionId, String accountNumber, String transactionType,
                      double amount, double balanceAfter, String description, 
                      LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = timestamp;
    }
    //getters for transaction fields
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    //provides a string representation of the transaction
    @Override
    public String toString() {
        return String.format("[%s] %s: BWP %.2f | Balance: BWP %.2f | %s",
            getFormattedTimestamp(), transactionType, amount, balanceAfter, description);
    }
    //provides a detailed report of the transaction
    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("Transaction ID: ").append(transactionId).append("\n");
        report.append("Account Number: ").append(accountNumber).append("\n");
        report.append("Type: ").append(transactionType).append("\n");
        report.append("Amount: BWP ").append(String.format("%.2f", amount)).append("\n");
        report.append("Balance After: BWP ").append(String.format("%.2f", balanceAfter)).append("\n");
        report.append("Date/Time: ").append(getFormattedTimestamp()).append("\n");
        report.append("Description: ").append(description).append("\n");
        return report.toString();
    }
}
