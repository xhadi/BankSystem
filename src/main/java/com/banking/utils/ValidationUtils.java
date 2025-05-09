package com.banking.utils;

import com.banking.auth.User;
import com.banking.config.Config;

import java.util.ArrayList;
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
    public static boolean isValidUsername(String username, ArrayList<User> existingUsers) {
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
        // Regex for DD-MM-YYYY format
        String regex = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19|20)\\d\\d$";
        if (!Pattern.matches(regex, dateOfBirth)) {
            return false;
        }
        
        String[] parts = dateOfBirth.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        
        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentYear = now.get(java.util.Calendar.YEAR);
        int currentMonth = now.get(java.util.Calendar.MONTH) + 1; // Month is 0-based
        int currentDay = now.get(java.util.Calendar.DAY_OF_MONTH);
    
        // Validate date (e.g., February 30 is invalid)
        try {
            java.util.Calendar dob = java.util.Calendar.getInstance();
            dob.setLenient(false);
            dob.set(year, month - 1, day); // Month is 0-based in Calendar
            dob.getTimeInMillis(); // Triggers validation
        } catch (Exception e) {
            return false;
        }
    
        // Age check
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
     * @param existingUsers list of existing users to check against
     * @return true if valid and unique, false otherwise
     */
    public static boolean isValidNationalID(String nationalID) {
        if (!Pattern.matches(Config.NATIONAL_ID_PATTERN, nationalID)) {
            return false;
        }
        return true;
    }

    public static boolean isUniqeNationalID(String nationalID, ArrayList<User> existingUsers){
        if(existingUsers == null){
            return false;
        }

        return existingUsers.stream()
                .noneMatch(user -> user.getNationalID().equals(nationalID));
    }

    /**
     * Validates account number matches required pattern
     * @param accountNumber account number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && Pattern.matches(Config.ACCOUNT_NUMBER_PATTERN, accountNumber);
    }
}