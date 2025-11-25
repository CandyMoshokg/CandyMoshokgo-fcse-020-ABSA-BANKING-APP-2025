package com.banking.model;
//represents a cheque account in the banking system
public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;
    //constructor initializes the cheque account with necessary details
    public ChequeAccount(String accountNumber, double initialBalance, String branch, 
                         Customer customer, String companyName, String companyAddress) {
        super(accountNumber, initialBalance, branch, customer);
        
        if (companyName == null || companyName.trim().isEmpty() ||
            companyAddress == null || companyAddress.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Cheque Account requires valid employment information (company name and address)"
            );
        }
        
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }
    //withdraws money from the cheque account if sufficient balance exists
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
        recordTransaction("WITHDRAWAL", amount, "Withdrawal from Cheque Account");
        return true;
    }
    //checks if the account has enough balance for a withdrawal
    @Override
    public double calculateInterest() {
        return 0.0; }
    
    @Override
    public String getAccountType() {
        return "Cheque Account";
    }
    //credits salary to the cheque account and records the transaction
    public boolean creditSalary(double amount, String employerReference) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        recordTransaction("SALARY", amount, 
            String.format("Salary credit from %s (Ref: %s)", companyName, employerReference));
        return true;
    }
    //updates the employment information associated with the cheque account
    public void updateEmploymentInfo(String companyName, String companyAddress) {
        if (companyName != null && !companyName.trim().isEmpty()) {
            this.companyName = companyName;
        }
        if (companyAddress != null && !companyAddress.trim().isEmpty()) {
            this.companyAddress = companyAddress;
        }
    }
    //getters for company name and address
    public String getCompanyName() {
        return companyName;
    }
    
    public String getCompanyAddress() {
        return companyAddress;
    }
    
    @Override
    public String toString() {
        return String.format("ChequeAccount[Number=%s, Balance=BWP %.2f, Employer=%s]",
            accountNumber, balance, companyName);
    }
}
