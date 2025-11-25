package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.*;

import java.util.List;

public class AccountController {
    //
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private Bank bank;
    private LoginController loginController;
    
    public AccountController(Bank bank) {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.bank = bank;
        this.loginController = LoginController.getInstance();
    }
    
    public AccountResult openSavingsAccount(String customerId, double initialBalance, String branch) {
        if (!loginController.hasPermission("OPEN_ACCOUNT")) {
            return new AccountResult(false, "You don't have permission to open accounts", null);
        }
        
        if (customerId == null || customerId.trim().isEmpty()) {
            return new AccountResult(false, "Customer ID is required", null);
        }
        
        if (initialBalance < 0) {
            return new AccountResult(false, "Initial balance cannot be negative", null);
        }
        
        if (branch == null || branch.trim().isEmpty()) {
            return new AccountResult(false, "Branch code is required", null);
        }
        
        try {
            SavingsAccount account = bank.openSavingsAccount(
                customerId.trim(),
                initialBalance,
                branch.trim()
            );
            
            boolean saved = accountDAO.save(account);
            
            if (saved) {
                if (initialBalance > 0) {
                    for (Transaction txn : account.getTransactionHistory()) {
                        transactionDAO.save(txn);
                    }
                }
                
                return new AccountResult(true, 
                    "Savings Account opened successfully: " + account.getAccountNumber(), 
                    account);
            } else {
                return new AccountResult(false, "Failed to save account to database", null);
            }
            
        } catch (IllegalArgumentException e) {
            return new AccountResult(false, e.getMessage(), null);
        } catch (Exception e) {
            return new AccountResult(false, "Error opening account: " + e.getMessage(), null);
        }
    }
    
    public AccountResult openInvestmentAccount(String customerId, double initialBalance, String branch) {
        if (!loginController.hasPermission("OPEN_ACCOUNT")) {
            return new AccountResult(false, "You don't have permission to open accounts", null);
        }
        
        if (customerId == null || customerId.trim().isEmpty()) {
            return new AccountResult(false, "Customer ID is required", null);
        }
        
        if (initialBalance < 500.00) {
            return new AccountResult(false, 
                "Investment Account requires minimum opening balance of BWP 500.00", null);
        }
        
        if (branch == null || branch.trim().isEmpty()) {
            return new AccountResult(false, "Branch code is required", null);
        }
        
        try {
            InvestmentAccount account = bank.openInvestmentAccount(
                customerId.trim(),
                initialBalance,
                branch.trim()
            );
            
            boolean saved = accountDAO.save(account);
            
            if (saved) {
                if (initialBalance > 0) {
                    for (Transaction txn : account.getTransactionHistory()) {
                        transactionDAO.save(txn);
                    }
                }
                
                return new AccountResult(true, 
                    "Investment Account opened successfully: " + account.getAccountNumber(), 
                    account);
            } else {
                return new AccountResult(false, "Failed to save account to database", null);
            }
            
        } catch (IllegalArgumentException e) {
            return new AccountResult(false, e.getMessage(), null);
        } catch (Exception e) {
            return new AccountResult(false, "Error opening account: " + e.getMessage(), null);
        }
    }
    
    public AccountResult openChequeAccount(String customerId, double initialBalance, String branch,
                                          String companyName, String companyAddress) {
        if (!loginController.hasPermission("OPEN_ACCOUNT")) {
            return new AccountResult(false, "You don't have permission to open accounts", null);
        }
        
        if (customerId == null || customerId.trim().isEmpty()) {
            return new AccountResult(false, "Customer ID is required", null);
        }
        
        if (initialBalance < 0) {
            return new AccountResult(false, "Initial balance cannot be negative", null);
        }
        
        if (branch == null || branch.trim().isEmpty()) {
            return new AccountResult(false, "Branch code is required", null);
        }
        
        if (companyName == null || companyName.trim().isEmpty()) {
            return new AccountResult(false, "Company name is required for Cheque Account", null);
        }
        
        if (companyAddress == null || companyAddress.trim().isEmpty()) {
            return new AccountResult(false, "Company address is required for Cheque Account", null);
        }
        
        try {
            ChequeAccount account = bank.openChequeAccount(
                customerId.trim(),
                initialBalance,
                branch.trim(),
                companyName.trim(),
                companyAddress.trim()
            );
            
            boolean saved = accountDAO.save(account);
            
            if (saved) {
                if (initialBalance > 0) {
                    for (Transaction txn : account.getTransactionHistory()) {
                        transactionDAO.save(txn);
                    }
                }
                
                return new AccountResult(true, 
                    "Cheque Account opened successfully: " + account.getAccountNumber(), 
                    account);
            } else {
                return new AccountResult(false, "Failed to save account to database", null);
            }
            
        } catch (IllegalArgumentException e) {
            return new AccountResult(false, e.getMessage(), null);
        } catch (Exception e) {
            return new AccountResult(false, "Error opening account: " + e.getMessage(), null);
        }
    }
    
    public TransactionResult deposit(String accountNumber, double amount) {
        if (!loginController.hasPermission("DEPOSIT")) {
            return new TransactionResult(false, "You don't have permission to make deposits", 0);
        }
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new TransactionResult(false, "Account number is required", 0);
        }
        
        if (amount <= 0) {
            return new TransactionResult(false, "Deposit amount must be positive", 0);
        }
        
        try {
            Account account = accountDAO.findByAccountNumber(accountNumber.trim());
            
            if (account == null) {
                return new TransactionResult(false, "Account not found: " + accountNumber, 0);
            }
            
            boolean success = account.deposit(amount);
            
            if (success) {
                accountDAO.updateBalance(account);
                
                List<Transaction> transactions = account.getTransactionHistory();
                if (!transactions.isEmpty()) {
                    Transaction latestTxn = transactions.get(transactions.size() - 1);
                    transactionDAO.save(latestTxn);
                }
                
                return new TransactionResult(true, 
                    String.format("Deposit successful. New balance: BWP %.2f", account.getBalance()),
                    account.getBalance());
            } else {
                return new TransactionResult(false, "Deposit failed", account.getBalance());
            }
            
        } catch (Exception e) {
            return new TransactionResult(false, "Error processing deposit: " + e.getMessage(), 0);
        }
    }
    
    public TransactionResult withdraw(String accountNumber, double amount) {
        if (!loginController.hasPermission("WITHDRAW")) {
            return new TransactionResult(false, "You don't have permission to make withdrawals", 0);
        }
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new TransactionResult(false, "Account number is required", 0);
        }
        
        if (amount <= 0) {
            return new TransactionResult(false, "Withdrawal amount must be positive", 0);
        }
        
        try {
            Account account = accountDAO.findByAccountNumber(accountNumber.trim());
            
            if (account == null) {
                return new TransactionResult(false, "Account not found: " + accountNumber, 0);
            }
            
            if (account instanceof SavingsAccount) {
                return new TransactionResult(false, 
                    "Withdrawals are not permitted on Savings Accounts", 
                    account.getBalance());
            }
            
            boolean success = account.withdraw(amount);
            
            if (success) {
                accountDAO.updateBalance(account);
                
                List<Transaction> transactions = account.getTransactionHistory();
                if (!transactions.isEmpty()) {
                    Transaction latestTxn = transactions.get(transactions.size() - 1);
                    transactionDAO.save(latestTxn);
                }
                
                return new TransactionResult(true, 
                    String.format("Withdrawal successful. New balance: BWP %.2f", account.getBalance()),
                    account.getBalance());
            } else {
                return new TransactionResult(false, 
                    "Insufficient balance for withdrawal", 
                    account.getBalance());
            }
            
        } catch (Exception e) {
            return new TransactionResult(false, "Error processing withdrawal: " + e.getMessage(), 0);
        }
    }
    
    public BalanceResult getBalance(String accountNumber) {
        if (!loginController.hasPermission("VIEW_BALANCE")) {
            return new BalanceResult(false, "You don't have permission to view balances", 0, null);
        }
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new BalanceResult(false, "Account number is required", 0, null);
        }
        
        Account account = accountDAO.findByAccountNumber(accountNumber.trim());
        
        if (account != null) {
            return new BalanceResult(true, "Balance retrieved", account.getBalance(), account);
        } else {
            return new BalanceResult(false, "Account not found", 0, null);
        }
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        if (!loginController.hasPermission("VIEW_TRANSACTIONS")) {
            return List.of();
        }
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return List.of();
        }
        
        return transactionDAO.findByAccountNumber(accountNumber.trim());
    }
    
    public InterestResult processMonthlyInterest() {
        if (!loginController.hasPermission("OVERRIDE_LIMIT")) {
            return new InterestResult(false, "You don't have permission to process interest", 0, 0);
        }
        
        try {
            List<Account> allAccounts = accountDAO.findAll();
            int accountsProcessed = 0;
            double totalInterestPaid = 0.0;
            
            for (Account account : allAccounts) {
                if (account instanceof SavingsAccount || account instanceof InvestmentAccount) {
                    double interestAmount = account.calculateInterest();
                    
                    if (interestAmount > 0) {
                        account.applyInterest();
                        accountDAO.updateBalance(account);
                        
                        List<Transaction> transactions = account.getTransactionHistory();
                        if (!transactions.isEmpty()) {
                            Transaction latestTxn = transactions.get(transactions.size() - 1);
                            transactionDAO.save(latestTxn);
                        }
                        
                        accountsProcessed++;
                        totalInterestPaid += interestAmount;
                    }
                }
            }
            
            return new InterestResult(true, 
                String.format("Interest processed for %d accounts. Total interest: BWP %.2f", 
                    accountsProcessed, totalInterestPaid),
                accountsProcessed, 
                totalInterestPaid);
                
        } catch (Exception e) {
            return new InterestResult(false, 
                "Error processing interest: " + e.getMessage(), 0, 0);
        }
    }
    
    public List<Account> getCustomerAccounts(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return List.of();
        }
        return accountDAO.findByCustomerId(customerId.trim());
    }
    
    public AccountStatistics getAccountStatistics() {
        int savingsCount = accountDAO.countByType("Savings Account");
        int investmentCount = accountDAO.countByType("Investment Account");
        int chequeCount = accountDAO.countByType("Cheque Account");
        int totalCount = savingsCount + investmentCount + chequeCount;
        
        return new AccountStatistics(savingsCount, investmentCount, chequeCount, totalCount);
    }
    
    public static class AccountResult {
        private final boolean success;
        private final String message;
        private final Account account;
        
        public AccountResult(boolean success, String message, Account account) {
            this.success = success;
            this.message = message;
            this.account = account;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Account getAccount() { return account; }
    }
    
    public static class TransactionResult {
        private final boolean success;
        private final String message;
        private final double newBalance;
        
        public TransactionResult(boolean success, String message, double newBalance) {
            this.success = success;
            this.message = message;
            this.newBalance = newBalance;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public double getNewBalance() { return newBalance; }
    }
    
    public static class BalanceResult {
        private final boolean success;
        private final String message;
        private final double balance;
        private final Account account;
        
        public BalanceResult(boolean success, String message, double balance, Account account) {
            this.success = success;
            this.message = message;
            this.balance = balance;
            this.account = account;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public double getBalance() { return balance; }
        public Account getAccount() { return account; }
    }
    
    public static class InterestResult {
        private final boolean success;
        private final String message;
        private final int accountsProcessed;
        private final double totalInterest;
        
        public InterestResult(boolean success, String message, int accountsProcessed, double totalInterest) {
            this.success = success;
            this.message = message;
            this.accountsProcessed = accountsProcessed;
            this.totalInterest = totalInterest;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public int getAccountsProcessed() { return accountsProcessed; }
        public double getTotalInterest() { return totalInterest; }
    }
    
    public static class AccountStatistics {
        private final int savingsCount;
        private final int investmentCount;
        private final int chequeCount;
        private final int totalCount;
        
        public AccountStatistics(int savingsCount, int investmentCount, int chequeCount, int totalCount) {
            this.savingsCount = savingsCount;
            this.investmentCount = investmentCount;
            this.chequeCount = chequeCount;
            this.totalCount = totalCount;
        }
        
        public int getSavingsCount() { return savingsCount; }
        public int getInvestmentCount() { return investmentCount; }
        public int getChequeCount() { return chequeCount; }
        public int getTotalCount() { return totalCount; }
    }
}