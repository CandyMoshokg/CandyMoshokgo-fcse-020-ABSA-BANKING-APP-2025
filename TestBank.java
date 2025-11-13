public class TestBank {
    public static void main(String[] args) {
        Customer john = new Customer("C001", "John", "Doe", "123 Main St", "password123");
        
        SavingsAccount savings = new SavingsAccount("SA001", 1000.0, "Main Branch", john);
        InvestmentAccount investment = new InvestmentAccount("INV001", 600.0, "Main Branch", john);
        
        savings.deposit(500.0);
        System.out.println("Savings balance: " + savings.getBalance());
        
        investment.deposit(200.0);
        System.out.println("Investment balance: " + investment.getBalance());
        
        System.out.println("Savings interest: " + savings.calculateInterest());
        System.out.println("Investment interest: " + investment.calculateInterest());
        
        System.out.println("All tests passed!");
    }
}
