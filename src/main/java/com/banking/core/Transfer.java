package com.banking.core;
import com.banking.data.DataAccess;

public class Transfer extends Transaction {
    private String targetAccountNumber;

    public Transfer(String fromAccountNumber, String targetAccountNumber, double amount) {
        super(fromAccountNumber, amount);
        this.targetAccountNumber = targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }
    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }
    
    public boolean execute(String sender, String receiver, double amount){
        try{
            DataAccess dataAccess = new DataAccess();
            System.out.println("Transfering $"+amount+" from accont: "+sender+" to account: "+receiver);
            dataAccess.updateAccountBalance(sender, -amount);
            dataAccess.updateAccountBalance(receiver, amount);
            return true;
        }
        catch(Exception e){
            System.out.println("Transfering failed: " + e.getMessage());
            return false;

        }
    }

    public void printTransactionConfirmation(Account fromAccount, Account toAccount,
            double amount, double newBalance) {
        super.printTransactionConfirmation("Transfer", fromAccount, toAccount,
            amount, this.getTimestamp(), this.getTransactionID(), newBalance);
    }
}