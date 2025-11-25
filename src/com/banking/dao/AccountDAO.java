package com.banking.dao;

import com.banking.database.DatabaseManager;
import com.banking.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private DatabaseManager dbManager;
    private CustomerDAO customerDAO;
    
    public AccountDAO() {
        //sets up connection to the databse
        this.dbManager = DatabaseManager.getInstance();
        this.customerDAO = new CustomerDAO();// creats customerDAO to fetch customer into whn neede
    }
    //adds account to the databse an handles account types
    public boolean save(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, " +
                     "company_name, company_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getCustomer().getCustomerId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());
            
                if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(6, chequeAccount.getCompanyName());
                pstmt.setString(7, chequeAccount.getCompanyAddress());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
            return false;
        }
    }
    //looks for an account in the databse an returns objct if found ,otherwise null
    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createAccountFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding account: " + e.getMessage());
        }
        
        return null;
    }
    //gets all acounts linked to a specific customer id
    public List<Account> findByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY date_opened";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Account account = createAccountFromResultSet(rs);
                if (account != null) {
                    accounts.add(account);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding accounts for customer: " + e.getMessage());
        }
        
        return accounts;
    }
    //changes the balance of account in the databse
    public boolean updateBalance(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getAccountNumber());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
            return false;
        }
    }
  //removes an account from databse
    public boolean delete(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
    //chcks all accounts from databse
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY account_number";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Account account = createAccountFromResultSet(rs);
                if (account != null) {
                    accounts.add(account);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all accounts: " + e.getMessage());
        }
        
        return accounts;
    }
    
    //Converts a database row into the right type of Account object
    private Account createAccountFromResultSet(ResultSet rs) throws SQLException {
        String accountNumber = rs.getString("account_number");
        String customerId = rs.getString("customer_id");
        String accountType = rs.getString("account_type");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");
        
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            System.err.println("Customer not found for account: " + accountNumber);
            return null;
        }
        
            Account account = null;
        
        switch (accountType) {
            case "Savings Account":
                account = new SavingsAccount(accountNumber, balance, branch, customer);
                break;
                
            case "Investment Account":
                account = new InvestmentAccount(accountNumber, balance, branch, customer);
                break;
                
            case "Cheque Account":
                String companyName = rs.getString("company_name");
                String companyAddress = rs.getString("company_address");
                account = new ChequeAccount(accountNumber, balance, branch, customer, 
                                          companyName, companyAddress);
                break;
                
            default:
                System.err.println("Unknown account type: " + accountType);
                return null;
        }
        
        return account;
    }
    //brings true if an account with the given number exists
    public boolean exists(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking account existence: " + e.getMessage());
        }
        
        return false;
    }
    //counts how many accounts of a certain type exists in the databse
    public int countByType(String accountType) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_type = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting accounts: " + e.getMessage());
        }
        
        return 0;
    }
}