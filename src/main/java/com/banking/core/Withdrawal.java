package com.banking.core;

import com.banking.data.DataAccess;

public class Withdrawal extends Transaction {

    public Withdrawal(String accountNumber, double amount) {
        super(accountNumber, amount);
    }

    
    public boolean execute(String accountNumber, double amount) {
        try{
            DataAccess dataAccess = new DataAccess();
            // Simulate withdraw operation
            System.out.println("Withdrawaling " + amount + " from account " + accountNumber);
            // Update the account balance in the database
            dataAccess.updateAccountBalance(accountNumber, amount);
            return true;
        } 
        catch (Exception e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            return false;
        }
    }

    public void printTransactionConfirmation(Account fromAccount, double amount, double newBalance) {
        super.printTransactionConfirmation("Withdrawal", fromAccount, null,
                                            amount, this.getTimestamp(), this.getTransactionID(),newBalance);
    }
}