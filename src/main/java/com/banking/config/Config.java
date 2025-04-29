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
    
    // User rules
    public static final int MAX_USERNAME_LENGTH = 8;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MIN_AGE = 18;

    // Transaction rules
    public static final double MIN_TRANSACTION_AMOUNT = 10.0;
}
