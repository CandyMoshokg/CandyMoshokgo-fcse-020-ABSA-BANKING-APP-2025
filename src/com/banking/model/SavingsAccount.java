package com.banking.model;
//represents a savings account in the banking system
public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly
    
    public SavingsAccount(String accountNumber, double initialBalance, 
                          String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer);
    }
    //withdrawals are not permitted from savings accounts
    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals are not permitted on Savings Accounts.");
        return false;
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
    
    @Override
    public String getAccountType() {
        return "Savings Account";
    }
    
    public double getInterestRate() {
        return INTEREST_RATE * 100; 
    }
    
    @Override
    public String toString() {
        return String.format("SavingsAccount[Number=%s, Balance=BWP %.2f, Interest=%.3f%%]",
            accountNumber, balance, getInterestRate());
    }
}
