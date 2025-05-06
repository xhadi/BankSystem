package com.banking.core;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.banking.core.Transaction.Status;

public class Account {
    private String accountNumber;
    private String nationalID;
    private AccountStatus status;
    private double balance;
    private String creationDate; // Changed to String
    private ArrayList<Transaction> transactions;

    /**
     * Generates a unique account number based on the current date and a random number
     * @return String unique account number
     */
    private String generateAccountNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMyyyy");
        String datePart = dateFormat.format(new Date());
        
        // Generate random 5-digit number
        Random random = new Random();
        String randomPart = String.format("%04d", random.nextInt(10000));
        
        return datePart + randomPart;
    }

    /**
     * Generates the creation date of the account as a formatted string
     * @return String current date and time in "MMM dd, yyyy HH:mm:ss" format
     */
    public String generateCreationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    /**
     * Constructor to create a new account
     * @param nationalID ID of the user who owns the account
     */
    public Account(String nationalID) {
        accountNumber = generateAccountNumber();
        this.nationalID = nationalID;
        status = AccountStatus.ACTIVE;
        balance = 0;
        creationDate = generateCreationDate(); // Now stores formatted string
        transactions = new ArrayList<>();
    }

    /**
     * Constructor to create an account with specific details
     * @param accountnumber the account number
     * @param nationalID ID of the user who owns the account
     * @param balance initial balance of the account
     * @param accountStatus status of the account
     * @param creationDate formatted date string in "MMM dd, yyyy HH:mm:ss" format
     */
    public Account(String accountnumber, String nationalID, double balance, 
                  AccountStatus accountStatus, String creationDate) {
        accountNumber = accountnumber;
        this.nationalID = nationalID;
        this.status = accountStatus;
        this.balance = balance;
        this.creationDate = creationDate;
        this.transactions = new ArrayList<>();
    }

    public boolean deposit(double amount) {
        Deposit deposit = new Deposit(accountNumber, amount);
        boolean success = deposit.execute(accountNumber, amount);
        if (!success) {
            deposit.setStatus(Status.FAILED);
            transactions.add(deposit);
            return false;
        }
        balance += amount;
        deposit.setStatus(Status.SUCCESS);
        transactions.add(deposit);
        deposit.printTransactionConfirmation(this, amount, balance);
        return true;
    }

    /**
     * Withdraws money from the account
     * @param amount the amount to withdraw
     * @return boolean indicating if the withdrawal was successful
     */
    public boolean withdraw(double amount) {
        Withdrawal withdrawal = new Withdrawal(accountNumber, amount);

        if (amount > balance) {
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
     * Transfers money to another account
     * @param targetAccount the account to transfer to
     * @param amount the amount to transfer
     * @return boolean indicating success
     */
    public boolean transfer(Account targetAccount, double amount) {
        Transfer transfer = new Transfer(this.getAccountNumber(), targetAccount.getAccountNumber(), amount);
        if(targetAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            transfer.setStatus(Status.FAILED);
            transactions.add(transfer);
            return false;
        }
        boolean success = transfer.execute(getAccountNumber(), targetAccount.getAccountNumber(), amount);
        if(!success) {
            transfer.setStatus(Status.FAILED);
            transactions.add(transfer);
            transfer.printTransactionConfirmation(this, targetAccount, amount, amount);
            return false;
        }
        balance -= amount;
        transfer.setStatus(Status.SUCCESS);
        transactions.add(transfer);
        transfer.printTransactionConfirmation(this, targetAccount, amount, balance);
        return true;
    }

    // Getters and setters
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

    public String getCreationDate() {
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

    public void displayInformations() {
        String line = "+-----------------------------------------------+";
        String format = "| %-18s | %-30s |%n";
        
        System.out.println(line);
        System.out.printf("| %-43s |%n", "ACCOUNT INFORMATION");
        System.out.println(line);
        System.out.printf(format, "Account Number", accountNumber);
        System.out.printf(format, "Status", status);
        System.out.printf(format, "Balance", String.format("$%.2f", balance));
        System.out.printf(format, "Creation Time", creationDate); // Direct string usage
        System.out.println(line);
    }

    public void activateAccount() {
        this.status = AccountStatus.ACTIVE;
        System.out.println("Account " + accountNumber + " activated");
    }
    
    public void deactivateAccount() {
        this.status = AccountStatus.INACTIVE;
        System.out.println("Account " + accountNumber + " deactivated");
    }
}