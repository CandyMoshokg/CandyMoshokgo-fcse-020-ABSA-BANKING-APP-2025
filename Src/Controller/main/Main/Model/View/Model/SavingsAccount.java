public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
    }
    
    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Cannot withdraw from savings account");
    }
    
    public double calculateInterest() {
        return getBalance() * 0.0005;
    }
 }