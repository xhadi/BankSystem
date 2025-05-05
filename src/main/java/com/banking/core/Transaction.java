package com.banking.core;
import java.time.LocalDateTime;

public abstract class Transaction {
    public enum Status {
        PENDING, SUCCESS, FAILED
    }

    private String transactionID;
    private String accountNumber;
    private double amount;
    private LocalDateTime timestamp;
    private Status status;

    public Transaction(String transactionID, String accountNumber, double amount, 
                        LocalDateTime timestamp, Status status) {
        this.transactionID = transactionID;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    public Transaction(String accountNumber, double amount) {
        transactionID = generateTransactionID();
        this.accountNumber = accountNumber;
        this.amount = amount;
        timestamp = LocalDateTime.now();
        status = Status.PENDING;
    }

    public String generateTransactionID(){
        // Generate a unique transaction ID based on the current timestamp and a random number
        return String.valueOf(System.currentTimeMillis()) + "-" + (int)(Math.random() * 10000);
    }

    public String getTransactionID(){
        return transactionID;
    }

    public String getTimestamp() {
        return timestamp.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public boolean execute() {
        return false;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public Status getStatus() {
        return status;
    }

    public void printTransactionConfirmation(String transactionType, 
                                                Account fromAccount,
                                                Account toAccount,
                                                double amount,
                                                String timestamp,
                                                String referenceNumber,
                                                double newBalance) {
    String separator = "════════════════════";
    String amountLabel = "";
    String accountAction = "";

    switch (transactionType.toUpperCase()) {
        case "DEPOSIT":
            amountLabel = "Amount Deposited";
            accountAction = "To Account";
            break;
        case "WITHDRAWAL":
            amountLabel = "Amount Withdrawn";
            accountAction = "From Account";
            break;
        case "TRANSFER":
            amountLabel = "Amount Transferred";
            accountAction = "From/To Accounts";
            break;
    }

    System.out.println("\n" + separator + " TRANSACTION CONFIRMATION " + separator);
    System.out.printf("%-25s: %s%n", "Transaction Type", transactionType.toUpperCase());

    // Show accounts based on transaction type
    if (transactionType.equalsIgnoreCase("TRANSFER")) {
        System.out.printf("%-25s: %s → %s%n", accountAction, 
                        fromAccount.getAccountNumber(), 
                        toAccount.getAccountNumber());
    } 
    else{
        if (!transactionType.equalsIgnoreCase("DEPOSIT")) {
            System.out.printf("%-25s: %s%n", accountAction, fromAccount.getAccountNumber());
        } 
        else {
            System.out.printf("%-25s: %s%n", accountAction, toAccount.getAccountNumber());
        }
    }

    System.out.printf("%-25s: $%,.2f%n", amountLabel, amount);
    System.out.printf("%-25s: %s%n", "Transaction Date", timestamp);
    System.out.printf("%-25s: %s%n", "Reference Number", referenceNumber);
    System.out.println(separator + "═══════════════════════════" + separator);
    
    // Balance message based on transaction type
    String balanceLabel = transactionType.equalsIgnoreCase("DEPOSIT") 
                        ? "New Recipient Balance" 
                        : "New Account Balance";
    System.out.printf("%-25s: $%,.2f%n%n", balanceLabel, newBalance);
}
}