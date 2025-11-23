import java.util.Scanner;

public class TextBankingSystem {
    private static BankController bankController = new BankController();
    private static Scanner scanner = new Scanner(System.in);
    private static Customer currentCustomer = null;
    
    public static void main(String[] args) {
        System.out.println("=== ABSA BANKING SYSTEM ===");
        showMainMenu();
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Register New Customer");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    System.out.println("Thank you for banking with ABSA!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        currentCustomer = bankController.login(customerId, password);
        
        if (currentCustomer != null) {
            System.out.println("Login successful! Welcome " + currentCustomer.getFirstName());
            showCustomerMenu();
        } else {
            System.out.println("Login failed! Try: C001/pass1 or C002/pass2");
        }
    }
    
    private static void registerCustomer() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        // Create new customer
        Customer newCustomer = new Customer(customerId, firstName, surname, address, password);
        
        // Add to bank controller (you might need to modify BankController to handle this)
        System.out.println("Customer " + firstName + " " + surname + " registered successfully!");
        System.out.println("You can now login with ID: " + customerId);
    }
    
    private static void showCustomerMenu() {
        while (currentCustomer != null) {
            System.out.println("\n--- CUSTOMER DASHBOARD ---");
            System.out.println("Welcome, " + currentCustomer.getFirstName() + " " + currentCustomer.getSurname());
            System.out.println("1. View Accounts");
            System.out.println("2. Make Deposit");
            System.out.println("3. Open New Account");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAccounts();
                    break;
                case 2:
                    makeDeposit();
                    break;
                case 3:
                    openNewAccount();
                    break;
                case 4:
                    currentCustomer = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void viewAccounts() {
        System.out.println("\n--- YOUR ACCOUNTS ---");
        var accounts = bankController.getCustomerAccounts(currentCustomer);
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountNumber() + 
                             " - " + account.getClass().getSimpleName() + 
                             " - Balance: BWP " + account.getBalance());
        }
    }
    
    private static void makeDeposit() {
        System.out.println("\n--- MAKE DEPOSIT ---");
        var accounts = bankController.getCustomerAccounts(currentCustomer);
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts available for deposit.");
            return;
        }
        
        viewAccounts();
        System.out.print("Select account number (1-" + accounts.size() + "): ");
        int accountIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (accountIndex < 0 || accountIndex >= accounts.size()) {
            System.out.println("Invalid account selection!");
            return;
        }
        
        System.out.print("Enter deposit amount: BWP ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        Account selectedAccount = accounts.get(accountIndex);
        selectedAccount.deposit(amount);
        
        System.out.println("Deposit successful! New balance: BWP " + selectedAccount.getBalance());
    }
    
    private static void openNewAccount() {
        System.out.println("\n--- OPEN NEW ACCOUNT ---");
        System.out.println("1. Savings Account (No withdrawals, 0.05% interest)");
        System.out.println("2. Investment Account (BWP 500 minimum, 5% interest)");
        System.out.println("3. Cheque Account (For salary deposits)");
        System.out.print("Choose account type: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter initial deposit: BWP ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();
        
        String accountNumber = "";
        Account newAccount = null;
        
        switch (choice) {
            case 1:
                accountNumber = "SA" + (bankController.getCustomerAccounts(currentCustomer).size() + 1);
                newAccount = new SavingsAccount(accountNumber, initialDeposit, "Main Branch", currentCustomer);
                break;
            case 2:
                if (initialDeposit < 500) {
                    System.out.println("Error: Investment account requires minimum BWP 500.00");
                    return;
                }
                accountNumber = "INV" + (bankController.getCustomerAccounts(currentCustomer).size() + 1);
                newAccount = new InvestmentAccount(accountNumber, initialDeposit, "Main Branch", currentCustomer);
                break;
            case 3:
                System.out.print("Enter company name: ");
                String companyName = scanner.nextLine();
                System.out.print("Enter company address: ");
                String companyAddress = scanner.nextLine();
                accountNumber = "CH" + (bankController.getCustomerAccounts(currentCustomer).size() + 1);
                newAccount = new ChequeAccount(accountNumber, initialDeposit, "Main Branch", currentCustomer, companyName, companyAddress);
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        // Add account to customer
        currentCustomer.addAccount(newAccount);
        System.out.println("Account created successfully! Account Number: " + accountNumber);
    }
}
