import java.util.List;

public class AccountController {
    
    public boolean deposit(Account account, double amount) {
        try {
            account.deposit(amount);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
            return false;
        }
    }
    
    public boolean withdraw(Account account, double amount) {
        try {
            account.withdraw(amount);
            return true;
        } catch (Exception e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            return false;
        }
    }
    
    public double calculateInterest(Account account) {
        if (account instanceof SavingsAccount) {
            return ((SavingsAccount) account).calculateInterest();
        } else if (account instanceof InvestmentAccount) {
            return ((InvestmentAccount) account).calculateInterest();
        }
        return 0.0; // No interest for Cheque accounts
    }
    
    public String getAccountType(Account account) {
        if (account instanceof SavingsAccount) {
            return "Savings Account";
        } else if (account instanceof InvestmentAccount) {
            return "Investment Account";
        } else if (account instanceof ChequeAccount) {
            return "Cheque Account";
        }
        return "Unknown Account Type";
    }
    
    public boolean canWithdraw(Account account) {
        return !(account instanceof SavingsAccount);
    }
    
    public String openNewAccount(Customer customer, String accountType, double initialDeposit, 
                                String companyName, String companyAddress) {
        try {
            String accountNumber = generateAccountNumber(customer, accountType);
            Account newAccount = null;
            
            switch (accountType.toUpperCase()) {
                case "SAVINGS":
                    newAccount = new SavingsAccount(accountNumber, initialDeposit, "Main Branch", customer);
                    break;
                case "INVESTMENT":
                    if (initialDeposit < 500.0) {
                        return "Error: Investment account requires minimum BWP 500.00";
                    }
                    newAccount = new InvestmentAccount(accountNumber, initialDeposit, "Main Branch", customer);
                    break;
                case "CHEQUE":
                    if (companyName == null || companyName.trim().isEmpty()) {
                        return "Error: Company name is required for cheque account";
                    }
                    newAccount = new ChequeAccount(accountNumber, initialDeposit, "Main Branch", customer, 
                                                 companyName, companyAddress);
                    break;
                default:
                    return "Error: Invalid account type";
            }
            
            customer.addAccount(newAccount);
            return "Success: " + accountType + " account opened with number: " + accountNumber;
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private String generateAccountNumber(Customer customer, String accountType) {
        String prefix = "";
        switch (accountType.toUpperCase()) {
            case "SAVINGS": prefix = "SA"; break;
            case "INVESTMENT": prefix = "INV"; break;
            case "CHEQUE": prefix = "CH"; break;
        }
        
        int accountCount = customer.getAccounts().size() + 1;
        return prefix + String.format("%03d", accountCount);
    }
    
    public double getTotalBalance(Customer customer) {
        double total = 0.0;
        for (Account account : customer.getAccounts()) {
            total += account.getBalance();
        }
        return total;
    }
}