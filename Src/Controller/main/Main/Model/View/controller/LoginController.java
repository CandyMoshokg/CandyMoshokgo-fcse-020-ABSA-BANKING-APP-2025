import java.util.ArrayList;
import java.util.List;

public class LoginController {
    private List<Customer> customers;
    
    public LoginController() {
        this.customers = new ArrayList<>();
        setupTestCustomers();
    }
    
    private void setupTestCustomers() {
        // Create test customers
        Customer customer1 = new Customer("C001", "General", "Motaso", "345 Block 3", "pass1");
        Customer customer2 = new Customer("C002", "Mbatsi", "Asambe", "786 Newstance", "pass2");
        Customer customer3 = new Customer("C003", "tshepho", "Thato", "212 Block 7", "pass3");
        
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        
        // Create accounts for customers
        SavingsAccount savings1 = new SavingsAccount("SA001", 3000.00, "Main Branch", customer1);
        InvestmentAccount investment1 = new InvestmentAccount("INV001", 600.0, "Main Branch", customer1);
        
        SavingsAccount savings2 = new SavingsAccount("SA002", 1500.00, "Main Branch", customer2);
        ChequeAccount cheque2 = new ChequeAccount("CH001", 2000.0, "Main Branch", customer2, "ABC Company", "123 Business St");
        
        SavingsAccount savings3 = new SavingsAccount("SA003", 800.0, "Main Branch", customer3);
        
        // Add accounts to customers
        customer1.addAccount(savings1);
        customer1.addAccount(investment1);
        customer2.addAccount(savings2);
        customer2.addAccount(cheque2);
        customer3.addAccount(savings3);
    }
    
    public Customer login(String customerId, String password) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId) && 
                customer.checkPassword(password)) {
                return customer;
            }
        }
        return null; // Login failed
    }
    
    public boolean registerCustomer(String customerId, String firstName, String surname, 
                                  String address, String password) {
        // Check if customer ID already exists
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return false; // Customer ID already exists
            }
        }
        
        // Create new customer
        Customer newCustomer = new Customer(customerId, firstName, surname, address, password);
        customers.add(newCustomer);
        return true;
    }
    
    // Get all customers (for testing)
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
}