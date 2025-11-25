package com.banking.model;

import com.banking.interfaces.Authenticatable;
import java.util.HashSet;
import java.util.Set;
//represents a user in the banking system with authentication capabilities
public class User implements Authenticatable {
    private String userId;
    private String username;
    private String passwordHash; 
    private String role; 
    private boolean authenticated;
    private Set<String> permissions;
    //constructor initializes user details and permissions based on role
    public User(String userId, String username, String passwordHash, String role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.authenticated = false;
        this.permissions = new HashSet<>();
        initializePermissions();
    }
    //sets up permissions based on user role
    private void initializePermissions() {
        switch (role.toUpperCase()) {
            case "ADMIN":
                permissions.add("CREATE_USER");
                permissions.add("DELETE_USER");
                permissions.add("VIEW_ALL_ACCOUNTS");
            case "MANAGER":
                permissions.add("CLOSE_ACCOUNT");
                permissions.add("OVERRIDE_LIMIT");
            case "TELLER":
                permissions.add("CREATE_CUSTOMER");
                permissions.add("OPEN_ACCOUNT");
                permissions.add("DEPOSIT");
                permissions.add("WITHDRAW");
                permissions.add("VIEW_BALANCE");
                permissions.add("VIEW_TRANSACTIONS");
                break;
            default:
                break;
        }
    }
    //implements authentication by verifying userId and password
    @Override
    public boolean authenticate(String userId, String password) {
        if (this.userId.equals(userId) && this.passwordHash.equals(hashPassword(password))) {
            this.authenticated = true;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hasPermission(String userId, String permission) {
        if (!this.userId.equals(userId) || !authenticated) {
            return false;
        }
        return permissions.contains(permission);
    }
    
    @Override
    public void logout(String userId) {
        if (this.userId.equals(userId)) {
            this.authenticated = false;
        }
    }
    
    @Override
    public boolean isAuthenticated(String userId) {
        return this.userId.equals(userId) && this.authenticated;
    }
    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }
    //getters for user fields
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRole() {
        return role;
    }
    
    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    @Override
    public String toString() {
        return String.format("User[ID=%s, Username=%s, Role=%s, Authenticated=%s]",
            userId, username, role, authenticated);
    }
}
