public class TestBank {
    public static void main(String[] args) {
        
        Customer john = new Customer("C001", "General", "Motaso", "345 Block 3", "password123");
        
        // Create  xsavingsaccounts fr particular custi
        SavingsAccount savings = new SavingsAccount("SA001", 3000.0, "Main Branch", General);
        InvestmentAccount investment = new InvestmentAccount("INV001", 600.0, "Main Branch", General);
        
        // Test deposits frm savings
        savings.deposit(500.0);
        System.out.println("Savings balance: " + savings.getcurrentBalance());
        
        investment.deposit(200.0);
        System.out.println("Investment balance: " + investment.getcurrentBalance());
        
        // Test interest frm interests
        System.out.println("Savings interest: " + savings.calculateInterest());
        System.out.println("Investment interest: " + investment.calculateInterest());
        
        System.out.println("All tests passed!");
    }
}