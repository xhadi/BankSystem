package com.banking.utils;

import com.banking.auth.User;
import com.banking.config.Config;

import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtils {
    /**
     * Validates password meets security requirements defined in Config.PASSWORD_PATTERN
     * @param password password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(Config.PASSWORD_PATTERN, password);
    }
    
    /**
     * Validates amount is within the allowed range
     * @param amount amount to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= Config.MAX_TRANSACTION_AMOUNT;
    }
    
    /**
     * Validates phone number matches required pattern
     * @param phone phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(Config.PHONE_PATTERN, phone);
    }

    /**
     * Validates username matches required pattern and is unique
     * @param username username to validate
     * @param existingUsers list of existing users to check against
     * @return true if valid and unique, false otherwise
     */
    public static boolean isValidUsername(String username, List<User> existingUsers) {
        if (!Pattern.matches(Config.USERNAME_PATTERN, username)) {
            return false;
        }
        
        return existingUsers.stream()
                .noneMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Validates name matches required pattern
     * @param name name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && Pattern.matches(Config.NAME_PATTERN, name);
    }

    
    public static boolean isValidDateOfBirth(String dateOfBirth) {
        // Assuming dateOfBirth is in the format "DD-MM-YYYY"
        String regex = "^(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        if (!Pattern.matches(regex, dateOfBirth)) {
            return false;
        }
        
        String[] parts = dateOfBirth.split("-");
        int year = Integer.parseInt(parts[2]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[0]);
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
        int currentDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);

        // Check if the date is valid
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }

        // Check if the user is at least 18 years old
        if (currentYear - year < Config.MIN_AGE) {
            return false;
        } else if (currentYear - year == Config.MIN_AGE) {
            if (currentMonth < month || (currentMonth == month && currentDay < day)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates national ID matches required pattern
     * @param nationalID national ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNationalID(String nationalID) {
        return nationalID != null && Pattern.matches(Config.NATIONAL_ID_PATTERN, nationalID);
    }

    /**
     * Validates national ID matches required pattern and is unique
     * @param nationalID national ID to validate
     * @param existingUsers list of existing users to check against
     * @return true if valid and unique, false otherwise
     */
    public static boolean isValidNationalID(String nationalID, List<User> existingUsers) {
        if (nationalID == null || existingUsers == null) {
            return false;
        }
        
        // First check pattern
        if (!Pattern.matches(Config.NATIONAL_ID_PATTERN, nationalID)) {
            return false;
        }
        
        // Then check uniqueness
        return existingUsers.stream()
                .noneMatch(user -> user.getNationalID().equals(nationalID));
    }
}