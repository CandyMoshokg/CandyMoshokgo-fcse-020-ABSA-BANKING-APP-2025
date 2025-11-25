package com.banking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    //only one database manger exists throughout the application
    private static DatabaseManager instance;
    //points to an H2 in-memory database
    private static final String DB_URL = "jdbc:h2:mem:bankingdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    //private constructor to load the database driver
    private DatabaseManager() {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("Database driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found: " + e.getMessage());
        }
    }
    //returns the single instance of DatabaseManager and creates it if it doesn't exist
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    //opens a new connection to the H2 database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    //Starts database intilization by opening a connection
    public void initializeDatabase() {
        try (Connection conn = getConnection();
             //creates a statement object to execute SQL commands
             Statement stmt = conn.createStatement()) {
            
                //creates the users table if it doesn't exist
            String createUsersTable = 
                "CREATE TABLE IF NOT EXISTS users (" +
                "user_id VARCHAR(50) PRIMARY KEY, " +
                "username VARCHAR(100) NOT NULL, " +
                "password_hash VARCHAR(255) NOT NULL, " +
                "role VARCHAR(50) NOT NULL, " +
                "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.execute(createUsersTable);
          //creates the customers table to store customer information  
            String createCustomersTable = 
                "CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id VARCHAR(50) PRIMARY KEY, " +
                "first_name VARCHAR(100) NOT NULL, " +
                "surname VARCHAR(100) NOT NULL, " +
                "address VARCHAR(255), " +
                "phone_number VARCHAR(20), " +
                "email VARCHAR(100), " +
                "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            stmt.execute(createCustomersTable);
            //creates accounts table to store bank account information
            String createAccountsTable = 
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number VARCHAR(50) PRIMARY KEY, " +
                "customer_id VARCHAR(50) NOT NULL, " +
                "account_type VARCHAR(20) NOT NULL, " +
                "balance DECIMAL(15, 2) NOT NULL, " +
                "branch VARCHAR(50), " +
                "date_opened TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "company_name VARCHAR(200), " +
                "company_address VARCHAR(255), " +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
                ")";
            stmt.execute(createAccountsTable);
            //creates transactions table to track all account transactions
            String createTransactionsTable = 
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id VARCHAR(50) PRIMARY KEY, " +
                "account_number VARCHAR(50) NOT NULL, " +
                "transaction_type VARCHAR(20) NOT NULL, " +
                "amount DECIMAL(15, 2) NOT NULL, " +
                "balance_after DECIMAL(15, 2) NOT NULL, " +
                "description VARCHAR(255), " +
                "transaction_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number)" +
                ")";
            stmt.execute(createTransactionsTable);
            
            System.out.println("Database schema initialized successfully");
            insertDefaultUser(conn);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //checks if an admin user exists
    private void insertDefaultUser(Connection conn) {
        String checkUserSQL = "SELECT COUNT(*) FROM users WHERE user_id = 'admin'";
        String insertUserSQL = 
            "INSERT INTO users (user_id, username, password_hash, role) " +
            "VALUES ('admin', 'Administrator', ?, 'ADMIN')";
        
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(checkUserSQL)) {
            
            rs.next();
            if (rs.getInt(1) == 0) {
                try (var pstmt = conn.prepareStatement(insertUserSQL)) {
                    String passwordHash = String.valueOf("admin123".hashCode());
                    pstmt.setString(1, passwordHash);
                    pstmt.executeUpdate();
                    System.out.println("✓ Default admin user created (username: admin, password: admin123)");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating default user: " + e.getMessage());
        }
    }
    //tets if the databse connection can be opened succesfully
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
    //safely closes a database connection
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}