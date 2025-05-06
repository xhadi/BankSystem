package com.banking.auth;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

import com.banking.data.DataAccess;
public class AuthenticationService {
    /**
     * Attempts to log in a user with the given credentials
     * @param <T> Type parameter that extends User class
     * @param users List of users to check against
     * @param username The username attempting to log in
     * @param password The password to verify
     * @return User object if login successful, null otherwise
     */
    public static User login(ArrayList<User> users, String nationalID, String password, DataAccess dataAccess) {
        if (nationalID == null || password == null || users == null) {
            System.out.println("Invalid login attempt: Missing input");
            return null;
        }


        for (User user : users) {
            if (user.getNationalID().equals(nationalID) && AuthenticationService.verifyPassword(password, user.getPassword())) {
                return switch (user.getUserType()) {
                    case ADMIN -> new Admin(user);
                    case MANAGER -> new Manager(user);
                    case ENDUSER -> new EndUser(user, dataAccess);
                    default -> throw new IllegalArgumentException("Invalid user type");
                };
            }
        }
        return null; // Authentication failed
    }

    /**
     * Hashes a password using BCrypt.
     * @param password The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    /**
     * Verifies a password against a hashed password.
     * @param inputPassword The password to verify
     * @param hashedPassword The hashed password to check against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        return BCrypt.checkpw(inputPassword, hashedPassword);
    }
}