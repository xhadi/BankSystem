package com.banking.core;

import com.banking.data.DataAccess;

public class Deposit extends Transaction {

    public Deposit(String accountNumber, double amount) {
        super(accountNumber, amount);
    }

    
    public boolean execute(String accountNumber, double amount) {
        try{
            DataAccess dataAccess = new DataAccess();
            // Simulate deposit operation
            System.out.println("Depositing " + amount + " to account " + accountNumber);
            // Update the account balance in the database
            dataAccess.updateAccountBalance(accountNumber, amount);
            return true;
        } 
        catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
            return false;
        }
    }

    public void printTransactionConfirmation(Account toAccount, double amount, double newBalance) {
        super.printTransactionConfirmation("Deposit", null, toAccount,
                                            amount, this.getTimestamp(), this.getTransactionID(),newBalance);
    }
}