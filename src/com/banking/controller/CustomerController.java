package com.banking.controller;

import com.banking.dao.CustomerDAO;
import com.banking.model.Bank;
import com.banking.model.Customer;

import java.util.List;

public class CustomerController {
        // DAO for interacting with customer data in the database
    private CustomerDAO customerDAO;
    private Bank bank;
    private LoginController loginController;
    // constructor that set up an object connectin it to the data access objct
    public CustomerController(Bank bank) {
        this.customerDAO = new CustomerDAO();
        this.bank = bank;
        this.loginController = LoginController.getInstance();
    }
    //  // Registers a new customer in the system
    public CustomerResult registerCustomer(String firstName, String surname, String address,
                                          String phoneNumber, String email) {
        if (!loginController.hasPermission("CREATE_CUSTOMER")) {
            return new CustomerResult(false, "You don't have permission to register customers", null);
        }
        //checks that all the necessary information has been provided
        if (firstName == null || firstName.trim().isEmpty()) {
            return new CustomerResult(false, "First name is required", null);
        }
        
        if (surname == null || surname.trim().isEmpty()) {
            return new CustomerResult(false, "Surname is required", null);
        }
        
        if (address == null || address.trim().isEmpty()) {
            return new CustomerResult(false, "Address is required", null);
        }
        //chcks name formats
        if (!isValidName(firstName)) {
            return new CustomerResult(false, "First name contains invalid characters", null);
        }
        
        if (!isValidName(surname)) {
            return new CustomerResult(false, "Surname contains invalid characters", null);
        }
        
        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            return new CustomerResult(false, "Invalid email format", null);
        }
        
        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && !isValidPhone(phoneNumber)) {
            return new CustomerResult(false, "Invalid phone number format", null);
        }
        
        try {
            //creates a new, unique ID for a customer
            String customerId = generateUniqueCustomerId();
            
            Customer customer = new Customer(customerId, firstName.trim(), surname.trim(), address.trim());
            
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                customer.setPhoneNumber(phoneNumber.trim());
            }
            
            if (email != null && !email.trim().isEmpty()) {
                customer.setEmail(email.trim());
            }
             // Save to database
            boolean saved = customerDAO.save(customer);
            
            if (saved) {
                System.out.println("✓ Customer registered successfully: " + customerId);
                return new CustomerResult(true, "Customer registered successfully: " + customerId, customer);
            } else {
                return new CustomerResult(false, "Failed to save customer to database", null);
            }
            
        } catch (Exception e) {
            System.err.println("Error registering customer: " + e.getMessage());
            e.printStackTrace();
            return new CustomerResult(false, "Error registering customer: " + e.getMessage(), null);
        }
    }
    //creates a unique ID for a customer by combining the current time (timestamp) with a random number
    private String generateUniqueCustomerId() {
        String customerId;
        int attempts = 0;
        
        do {
            long timestamp = System.currentTimeMillis();
            int random = (int)(Math.random() * 1000);
            customerId = String.format("CUST-%d%03d", timestamp % 100000, random);
            attempts++;
             // Fallback if too many attempts
            if (attempts > 100) {
                customerId = "CUST-" + System.currentTimeMillis();
                break;
            }
            
        } while (customerDAO.exists(customerId)); 
        
        return customerId;
    }
    //chcks customer by id
    public CustomerResult getCustomer(String customerId) {
        if (!loginController.hasPermission("VIEW_BALANCE")) {
            return new CustomerResult(false, "You don't have permission to view customers", null);
        }
        //chcks input
        if (customerId == null || customerId.trim().isEmpty()) {
            return new CustomerResult(false, "Customer ID is required", null);
        }
        
        Customer customer = customerDAO.findById(customerId.trim());
        
        if (customer != null) {
            return new CustomerResult(true, "Customer found", customer);
        } else {
            return new CustomerResult(false, "Customer not found: " + customerId, null);
        }
    }
     // Updates an existing customer
    public CustomerResult updateCustomer(Customer customer) {
        if (!loginController.hasPermission("CREATE_CUSTOMER")) {
            return new CustomerResult(false, "You don't have permission to update customers", null);
        }
        
        if (customer == null) {
            return new CustomerResult(false, "Customer object is required", null);
        }
        //makes sure custi exists
        if (!customerDAO.exists(customer.getCustomerId())) {
            return new CustomerResult(false, "Customer does not exist: " + customer.getCustomerId(), null);
        }
        //update in the database
        boolean updated = customerDAO.update(customer);
        
        if (updated) {
            return new CustomerResult(true, "Customer updated successfully", customer);
        } else {
            return new CustomerResult(false, "Failed to update customer", null);
        }
    }
    //looks for customers whose names match a search term. If no name is provided, it just returns all customers
    public List<Customer> searchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return customerDAO.findAll();
        }
        return customerDAO.searchByName(searchTerm.trim());
    }
    //chcks all customers if user granted permission
    public List<Customer> getAllCustomers() {
        if (!loginController.hasPermission("VIEW_BALANCE")) {
            return List.of();
        }
        return customerDAO.findAll();
    }
        // Deletes a customer by ID
    public CustomerResult deleteCustomer(String customerId) {
        if (!loginController.hasPermission("DELETE_USER")) {
            return new CustomerResult(false, "You don't have permission to delete customers", null);
        }
        
        if (customerId == null || customerId.trim().isEmpty()) {
            return new CustomerResult(false, "Customer ID is required", null);
        }
        
        Customer customer = customerDAO.findById(customerId.trim());
        if (customer == null) {
            return new CustomerResult(false, "Customer not found", null);
        }
              // Prevent deletion if customer has accounts
        if (customer.getAccounts().size() > 0) {
            return new CustomerResult(false, "Cannot delete customer with existing accounts", null);
        }
        
        boolean deleted = customerDAO.delete(customerId.trim());
        
        if (deleted) {
            return new CustomerResult(true, "Customer deleted successfully", null);
        } else {
            return new CustomerResult(false, "Failed to delete customer", null);
        }
    }
    //gives total numbr of custumers
    public int getCustomerCount() {
        return customerDAO.findAll().size();
    }
    //checks that a name is written correctly, following the expected format
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("[a-zA-Z\\s'-]+");
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        return digitsOnly.length() >= 7 && digitsOnly.length() <= 15;
    }
        // Result object for customer operation
    public static class CustomerResult {
        private final boolean success;
        private final String message;
        private final Customer customer;
        
        public CustomerResult(boolean success, String message, Customer customer) {
            this.success = success;
            this.message = message;
            this.customer = customer;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Customer getCustomer() {
            return customer;
        }
    }
}