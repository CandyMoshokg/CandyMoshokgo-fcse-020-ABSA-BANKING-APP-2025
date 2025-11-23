public class InvestmentAccount extends Account {
    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
        if (balance < 500.0) {
            throw new IllegalArgumentException("Investment account requires minimum BWP 500.00");
        }
    }
    
    @Override
    public void withdraw(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
        setBalance(getBalance() - amount);
    }
    
    public double calculateInterest() {
        return getBalance() * 0.05;
    }
}