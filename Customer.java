import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String password;
    private List<Account> accounts;
    
    public Customer(String customerId, String firstName, String surname, String address, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.password = password;
        this.accounts = new ArrayList<>();
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public List<Account> getAccounts() { return accounts; }
    
    // Check if password is correct
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    // Add account to customer
    public void addAccount(Account account) {
        accounts.add(account);
    }
}
