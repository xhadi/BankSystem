package com.banking.core;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Account {
    private String accountNumber;
    private String userID;
    private AccountStatus status;
    private double balance;
    private Date creationDate;

    /*
     * Generates a unique account number based on the current date and a random number
     * @return String unique account number
     */
    private static String generateAccountNumber() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMyyyy");
        String datePart = dateFormat.format(new Date());
        
        // Generate random 5-digit number
        Random random = new Random();
        String randomPart = String.format("%04d", random.nextInt(10000));
        
        return datePart + " " + randomPart;
    }

    /**
     * Generates the creation date of the account
     * @return Date current date and time
     */
    public Date generateCreationDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date creationDate = dateFormat.parse(dateFormat.format(new Date()));
            return creationDate;
        } catch (java.text.ParseException e) {
            return new Date();
        }
    }

    /**
     * Constructor to create a new account
     * @param userID ID of the user who owns the account
     * @param balance initial balance of the account
     */
    public Account(String userID, double balance) {
        this.accountNumber = generateAccountNumber();
        this.userID = userID;
        this.status = AccountStatus.Active;
        this.balance = balance;
        this.creationDate = generateCreationDate();
    }

    /**
     * Constructor to create an account with a specific account number
     * @param accountNumber the account number
     * @param userID ID of the user who owns the account
     * @param balance initial balance of the account
     * @param status status of the account
     */
    public Account(String accountNumber, String userID, double balance, AccountStatus accountStatus, Date creationDate){
        this.accountNumber = accountNumber;
        this.userID = userID;
        this.status = accountStatus;
        this.balance = balance;
        this.creationDate = creationDate;

    }

    /*
     * Deposits money into the account
     * @param amount the amount to deposit
     * @return boolean indicating if the deposit was successful or not
     */
    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    /**
     * Withdraws money from the account
     * @param amount the amount to withdraw
     * @return boolean indicating if the withdrawal was successful or not
     */
    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Transfers money from this account to another account
     * @param targetAccount the account to transfer money to
     * @param amount the amount to transfer
     * @return boolean indicating if the transfer was successful
     */
    public boolean transfer(Account targetAccount, double amount) {
        if (this.withdraw(amount)) {
            targetAccount.deposit(amount);
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public String getUserID() {
        return userID;
    }
    public AccountStatus getAccountStatus() {
        return status;
    }
    public double getBalance() {
        return balance;
    }
    public Date getCreationDate() {
        return creationDate;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}