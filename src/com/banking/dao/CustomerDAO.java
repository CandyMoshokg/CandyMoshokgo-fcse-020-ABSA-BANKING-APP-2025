package com.banking.dao;

import com.banking.database.DatabaseManager;
import com.banking.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private DatabaseManager dbManager;
    
    public CustomerDAO() {
        //sets up connection to th databse
        this.dbManager = DatabaseManager.getInstance();
    }
    //saves a new customer to the databse
    public boolean save(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPhoneNumber());
            pstmt.setString(6, customer.getEmail());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving customer: " + e.getMessage());
            return false;
        }
    }
    //looks for a customer in the databse by id and returns the customer object if found, otherwise null
    public Customer findById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("address")
                );
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));
                return customer;
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding customer: " + e.getMessage());
        }
        
        return null;
    }
    //retrieves all customers 
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY surname, first_name";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("address")
                );
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }
        
        return customers;
    }
    //changes customer details in the databse
    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ?, " +
                     "phone_number = ?, email = ? WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getCustomerId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    //removes a customer from the databse
    public boolean delete(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    //looks for customers whose first name or surname contains the search term (case-insensitive)
    public List<Customer> searchByName(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE " +
                     "LOWER(first_name) LIKE ? OR LOWER(surname) LIKE ? " +
                     "ORDER BY surname, first_name";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("address")
                );
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        
        return customers;
    }
    //returns true if a customer with the given ID exists
    public boolean exists(String customerId) {
        String sql = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
        }
        
        return false;
    }
}