package com.banking.config;
public class Config {

    // File paths for data storage
    public static final String DATA_DIR = "data/";
    public static final String USERS_FILE = DATA_DIR + "users.csv";
    public static final String ACCOUNTS_FILE = DATA_DIR + "accounts.csv";

    // Password requirements
    public static final int MIN_PASSWORD_LENGTH = 8;
    // Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character.
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{" 
        + MIN_PASSWORD_LENGTH + ",}$";

    // Phone number pattern
    public static final String PHONE_PATTERN = "^\\+?[0-9]{1,3}?[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}$";
    // Username pattern
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{3,8}$";
    // Name pattern
    public static final String NAME_PATTERN = "^[a-zA-Z]{2,30}$";
    // National ID pattern
    public static final String NATIONAL_ID_PATTERN = "^[0-9]{10}$";

    // Account number pattern
    public static final String ACCOUNT_NUMBER_PATTERN = "^[0-9]{10}$";
    
    // Minimum age requirement
    public static final int MIN_AGE = 18;

    // Transaction rules
    public static final double MIN_TRANSACTION_AMOUNT = 10.0;
    public static final double MAX_TRANSACTION_AMOUNT = 10000.0;
}
