package com.banking.controller;

import com.banking.dao.UserDAO;
import com.banking.model.User;
//manages user authentication, session state, and permissions
public class LoginController {
    //communicate with database to save, read, update, or delete user information
    private UserDAO userDAO;
    private User currentUser; 
    //ensures only one controller exists
    private static LoginController instance; 
    //sets up the data access object (DAO) and starts with no user logged in (currentUser is null).
    public LoginController() {
        this.userDAO = new UserDAO();
        this.currentUser = null;
        instance = this;
    }
    
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }
    //checks if a user’s ID and password are correct
    //also brings failure or succss results
    public LoginResult login(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            return new LoginResult(false, "User ID cannot be empty", null);
        }
        
        if (password == null || password.trim().isEmpty()) {
            return new LoginResult(false, "Password cannot be empty", null);
        }
        //checks the user’s credentials by looking them up in the database
        User user = userDAO.authenticate(userId.trim(), password);
        
        if (user != null) {
            this.currentUser = user;
            user.authenticate(userId, password);
            
            return new LoginResult(true, "Login successful", user);
        } else {
            return new LoginResult(false, "Invalid user ID or password", null);
        }
    }
    // Logs out the current user and clears session
    public void logout() {
        if (currentUser != null) {
            currentUser.logout(currentUser.getUserId());
            currentUser = null;
        }
    }
        // Returns the currently logged-in user
    public User getCurrentUser() {
        return currentUser;
    }
    //verifies whether a user is currently logged in and has been successfully authenticated
    public boolean isLoggedIn() {
        return currentUser != null && currentUser.isAuthenticated(currentUser.getUserId());
    }
    
    public boolean hasPermission(String permission) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.hasPermission(currentUser.getUserId(), permission);
    }
    //registers a new user with given details
    public RegistrationResult registerUser(String userId, String username, String password, String role) {
        if (!hasPermission("CREATE_USER")) {
            return new RegistrationResult(false, "You don't have permission to create users");
        }
        //validate inputs
        if (userId == null || userId.trim().isEmpty()) {
            return new RegistrationResult(false, "User ID cannot be empty");
        }
        
        if (username == null || username.trim().isEmpty()) {
            return new RegistrationResult(false, "Username cannot be empty");
        }
        
        if (password == null || password.length() < 6) {
            return new RegistrationResult(false, "Password must be at least 6 characters");
        }
        
        if (role == null || (!role.equals("TELLER") && !role.equals("MANAGER") && !role.equals("ADMIN"))) {
            return new RegistrationResult(false, "Invalid role. Must be TELLER, MANAGER, or ADMIN");
        }
        
        if (userDAO.exists(userId.trim())) {
            return new RegistrationResult(false, "User ID already exists");
        }
        //save user to database
        boolean success = userDAO.saveWithPassword(userId.trim(), username.trim(), password, role);
        
        if (success) {
            return new RegistrationResult(true, "User registered successfully");
        } else {
            return new RegistrationResult(false, "Failed to register user. Please try again");
        }
    }
     //allows the current user to change their password
    public boolean changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        //verify current password
        User verifiedUser = userDAO.authenticate(currentUser.getUserId(), currentPassword);
        if (verifiedUser == null) {
            return false;
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            return false;
        }
        
        return userDAO.updatePassword(currentUser.getUserId(), newPassword);
    }
    //represents an object that stores the outcome of a login attempt
    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final User user;
        
        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public User getUser() {
            return user;
        }
    }
    //represents an object that stores the outcome of a user registration attempt
    public static class RegistrationResult {
        private final boolean success;
        private final String message;
        
        public RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}