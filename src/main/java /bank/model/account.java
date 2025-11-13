public abstract class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer customer;
    
    public Account(String accountNumber, double balance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public abstract void withdraw(double amount);
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
    
    protected void setBalance(double balance) {
        this.balance = balance;
    }
}