package com.banking.model;
//represents an investment account in the banking system
public class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MINIMUM_OPENING_BALANCE = 500.00;
    //constructor initializes the investment account with necessary details
    public InvestmentAccount(String accountNumber, double initialBalance, 
                             String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer);
        
        if (initialBalance < MINIMUM_OPENING_BALANCE) {
            throw new IllegalArgumentException(
                String.format("Investment Account requires minimum opening balance of BWP %.2f", 
                MINIMUM_OPENING_BALANCE)
            );
        }
    }
    //withdraws money from the investment account if sufficient balance exists
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (!hasSufficientBalance(amount)) {
            System.out.println("Insufficient balance for withdrawal.");
            return false;
        }
        balance -= amount;
        recordTransaction("WITHDRAWAL", amount, "Withdrawal from Investment Account");
        return true;
    }
    //calculates interest based on the current balance
    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
    @Override
    public String getAccountType() {
        return "Investment Account";
    }
    public double getInterestRate() {
        return INTEREST_RATE * 100;
    }
    public static double getMinimumOpeningBalance() {
        return MINIMUM_OPENING_BALANCE;
    }
    @Override
    public String toString() {
        return String.format("InvestmentAccount[Number=%s, Balance=BWP %.2f, Interest=%.1f%%]",
            accountNumber, balance, getInterestRate());
    }
}
