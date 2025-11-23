public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;
    
    public ChequeAccount(String accountNumber, double balance, String branch, Customer customer, 
                        String companyName, String companyAddress) {
        super(accountNumber, balance, branch, customer);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }
    
    @Override
    public void withdraw(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
        setBalance(getBalance() - amount);
    }
    
    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
}