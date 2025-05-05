package com.banking.auth;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
public class AuthenticationService {
    /**
     * Attempts to log in a user with the given credentials
     * @param <T> Type parameter that extends User class
     * @param users List of users to check against
     * @param username The username attempting to log in
     * @param password The password to verify
     * @return User object if login successful, null otherwise
     */
    public static <T extends User> T login(ArrayList<T> users, String nationalID, String password) {
        if (nationalID == null || password == null || users == null) {
            System.out.println("Invalid login attempt: Missing input");
            return null;
        }

        return users.stream()
                   .filter(user -> user.getNationalID().equals(nationalID))
                   .findFirst()
                   .filter(user -> {
                       if (!user.isActive()) {
                           System.out.println("Login failed: User is not active");
                           return false;
                       }
                       if (!verifyPassword(password, user.getPassword())) {
                           System.out.println("Login failed: Invalid password");
                           return false;
                       }
                       System.out.println("Login successful: Welcome " + user.getUsername());
                       return true;
                   })
                   .orElseGet(() -> {
                       System.out.println("Login failed: User not found");
                       return null;
                   });
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