package com.banking.auth;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
public class AuthenticationService {
    /**
     * Validates user credentials against the stored user data.
     * @param users List of users to validate against
     * @param userName The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    public static boolean validateCredentials(ArrayList<User> users, String userName, String password) {
        if (userName == null || password == null) {
            return false;
        }

        int userIndex = users.indexOf(users.stream()
                                    .filter(user -> user.getUserName().equals(userName))
                                    .findFirst()
                                    .orElse(null));

        if (userIndex == -1) {
            return false;
        }

        boolean isActive = users.get(userIndex).getStatus().equals("active");
        boolean matchPassword = verifyPassword(password, users.get(userIndex).getPassword());

        if(!isActive) {
            System.out.println("User is not active.");
            return false;
        }

        if (!matchPassword) {
            System.out.println("Invalid password.");
            return false;
        }

        System.out.println("User authenticated successfully.");
        return true;
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