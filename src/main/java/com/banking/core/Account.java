package com.banking.core;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.banking.core.Transaction.Status;

public class Account {
    private String accountNumber;
    private String nationalID;
    private AccountStatus status;
    private double balance;
    private Date creationDate;
    private ArrayList<Transaction> transactions;

    /**
     * Generates a unique account number based on the current date and a random number
     * @return String unique account number
     */
    private String generateAccountNumber() {
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
    public Account(String nationalID) {
        accountNumber = generateAccountNumber();
        this.nationalID = nationalID;
        this.status = AccountStatus.Active;
        this.balance = 0;
        this.creationDate = generateCreationDate();
        this.transactions = new ArrayList<>();
    }

    /**
     * Constructor to create an account with a specific account number
     * @param accountNumber the account number
     * @param userID ID of the user who owns the account
     * @param balance initial balance of the account
     * @param status status of the account
     */
    public Account(String accountnumber, String nationalID, double balance, AccountStatus accountStatus, Date creationDate){
        accountNumber = accountnumber;
        this.nationalID = nationalID;
        this.status = accountStatus;
        this.balance = balance;
        this.creationDate = creationDate;
        this.transactions = new ArrayList<Transaction>();
    }

    public boolean deposit(double amount) {
        Deposit deposit = new Deposit(accountNumber, amount);
        boolean success = deposit.execute(accountNumber,amount);
        if (!success) {
            deposit.setStatus(Status.FAILED);
            transactions.add(deposit);
            return false;
        }
        // Add the deposit transaction to the account's transaction history
        balance += amount;
        deposit.setStatus(Status.SUCCESS);
        transactions.add(deposit);
        deposit.printTransactionConfirmation(this, amount, balance);
        return true;
    }

    /**
     * Withdraws money from the account
     * @param amount the amount to withdraw
     * @return boolean indicating if the withdrawal was successful or not
     */
    public boolean withdraw(double amount) {
        Withdrawal withdrawal = new Withdrawal(accountNumber, amount);

        if (!(amount < balance)) {
            withdrawal.setStatus(Status.FAILED);
            transactions.add(withdrawal);
            return false;
        }
        boolean success = withdrawal.execute(accountNumber, -amount);
        if (!success) {
            withdrawal.setStatus(Status.FAILED);
            transactions.add(withdrawal);
            return false;
        }
        balance -= amount;
        withdrawal.setStatus(Status.SUCCESS);
        transactions.add(withdrawal);
        withdrawal.printTransactionConfirmation(this, amount, balance);
        return true;
    }

    /**
     * Transfers money from this account to another account
     * @param targetAccount the account to transfer money to
     * @param amount the amount to transfer
     * @return boolean indicating if the transfer was successful
     */
    public boolean transfer(Account targetAccount, double amount) {
        Transfer transfer = new Transfer(this.getAccountNumber(), targetAccount.getAccountNumber(), amount);
        if(targetAccount.getAccountStatus() == AccountStatus.Inactive){
            transfer.setStatus(Status.FAILED);
            transactions.add(transfer);
            return false;
        }
        boolean success;
        success = transfer.execute(getAccountNumber(), targetAccount.getAccountNumber(), amount);
        if(!success){
            transfer.setStatus(Status.FAILED);
            transactions.add(transfer);
            transfer.printTransactionConfirmation(this, targetAccount, amount, amount);
            return false;
        }

        transfer.setStatus(Status.SUCCESS);
        transactions.add(transfer);
        transfer.printTransactionConfirmation(this, targetAccount, amount, balance);
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public String getNationalID() {
        return nationalID;
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
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void displayInformations(){
        System.out.println("----The informations of the account----");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Status: " + status);
        System.out.println("Balance: $" + balance);
        System.out.println("Creation Time: " + creationDate);
    }
}