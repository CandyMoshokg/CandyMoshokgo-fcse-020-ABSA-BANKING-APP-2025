package com.banking.interfaces;
//this interface defines methods for user authentication and session management
public interface Authenticatable {
    //checks the user credentials and returns true if they are valid
    boolean authenticate(String userId, String password);
    boolean hasPermission(String userId, String permission);
    //logs out the user and ends their session
    void logout(String userId);
    //chcks if the user is currently logged in
    boolean isAuthenticated(String userId);
}
