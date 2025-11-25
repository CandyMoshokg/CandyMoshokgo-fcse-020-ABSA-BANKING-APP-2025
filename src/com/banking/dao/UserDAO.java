package com.banking.dao;

import com.banking.database.DatabaseManager;
import com.banking.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseManager dbManager;
    
    public UserDAO() {
        //sets up connection to the databse
        this.dbManager = DatabaseManager.getInstance();
    }
    //adds a new user to the database
    public boolean save(User user) {
        String sql = "INSERT INTO users (user_id, username, password_hash, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, hashPassword("temp"));
            pstmt.setString(4, user.getRole());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    //adds a new user with specified password
      public boolean saveWithPassword(String userId, String username, String password, String role) {
        String sql = "INSERT INTO users (user_id, username, password_hash, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, hashPassword(password));
            pstmt.setString(4, role);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving user with password: " + e.getMessage());
            return false;
        }
    }
    //looks for the user in the databse by id and returns the user object if found, otherwise null
      public User findById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        
        return null;
    }
  //checks user credentials and returns the user if authentication is successful
  public User authenticate(String userId, String password) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password_hash = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, hashPassword(password));
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = createUserFromResultSet(rs);
                System.out.println("User authenticated: " + user.getUsername());
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        
        return null;  }
    //retrieves list of all users
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        
        return users;
    }
    //retrieves users by their role
    public List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY username";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding users by role: " + e.getMessage());
        }
        
        return users;
    }
    //chges username for an existing user
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    //changes the password for an existing user
    public boolean updatePassword(String userId, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashPassword(newPassword));
            pstmt.setString(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
    
    //removes a user from database
    public boolean delete(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    //returns true if a user with the given ID exists in the database
    public boolean exists(String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
        }
        
        return false;
    }
    //returns the total number of users in the database
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        
        return 0;
    }
    //converts a database row into a User object
	private User createUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getString("user_id"),
            rs.getString("username"),
            rs.getString("password_hash"),
            rs.getString("role")
        );
    }
    
    private String hashPassword(String password) {
    
        return Integer.toString(password.hashCode());
    }
}
