package com.banking.auth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import org.jline.reader.LineReader;

import com.banking.core.Account;
import com.banking.data.DataAccess;
import com.banking.utils.ValidationUtils;

public class EndUser extends User {
    private ArrayList<Account> accounts = new ArrayList<>();

    public EndUser(){
        
    }
    
    public EndUser(User user, DataAccess dataAccess){
        super(user.getNationalID(), user.getFirstName(), user.getFatherName(), user.getFamilyName(), user.getDateOfBirth(), user.getPhone(),
        user.getUsername(), user.getPassword(), user.getUserType(), user.getStatus());
        ArrayList<Account> allAccounts = dataAccess.loadAllAccountsByUserID(user.getNationalID());
        if(allAccounts != null){
            accounts = allAccounts;
        }
    }

    public EndUser(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
    String username, String password){
        super(nationalID, firstName, fatherName, familyName, dateOfBirth, 
        phone, username, password);
    }

    public EndUser(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
                String username, String password, UserRole userType, boolean status){
        super(nationalID, firstName, fatherName, familyName, dateOfBirth, phone,
                username, password, userType, status);
    }

    public boolean deposit(LineReader reader, double amount) {
        if (!ValidationUtils.isValidAmount(amount) || amount % 10 != 0) {
            System.out.println("Deposit amount must be at least  or in increments of  and less than ,000.");
            return false;
        }

        if(accounts.isEmpty()) {
            System.out.println("No accounts available for deposit.");
            return false;
        }
    
        System.out.println("Choose one of your account(s) to deposit into:");
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String separator = "═══════════════════════════════════════════";
    
        // Display accounts
        System.out.println("Index       Account Number      Balance");
        System.out.println(separator);
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.printf("%-5d %-15s %18s%n", 
                i + 1, 
                acc.getAccountNumber(), 
                currencyFormat.format(acc.getBalance())
            );
        }
        System.out.println(separator);
        System.out.printf("[%d accounts]   Total Balance: %s%n",
            accounts.size(),
            currencyFormat.format(accounts.stream().mapToDouble(Account::getBalance).sum())
        );
    
        // Get account index
        int selectedIndex;
        while (true) {
            String input = reader.readLine("Enter the index of the account: ").trim();
            try {
                selectedIndex = Integer.parseInt(input);
                if (selectedIndex < 1 || selectedIndex > accounts.size()) {
                    System.out.println("Invalid index. Enter a number between 1 and " + accounts.size());
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a numeric index.");
            }
        }
    
        // Deposit into selected account
        Account selectedAccount = accounts.get(selectedIndex - 1);
        boolean success = selectedAccount.deposit(amount);
        if (!success) {
            System.out.println("Deposit failed. Please try again.");
            return false;
        }
    
        System.out.printf("Deposit successful. New balance: %s%n",
            currencyFormat.format(selectedAccount.getBalance())
        );
        return true;
    }

    public boolean withdrawal(LineReader reader, double amount) {
        if (!ValidationUtils.isValidAmount(amount) || amount % 10 != 0) {
            System.out.println("Invalid amount must be in increments of 10 and less than $10,000.");
            return false;
        }
    
        System.out.println("Choose one of your account(s) to withdraw from:");
        displayAccountSelection();
    
        try {
            int selectedIndex = Integer.parseInt(reader.readLine().trim());
            Account selectedAccount = getAccountByIndex(selectedIndex);
            
            if (selectedAccount == null) {
                System.out.println("Invalid index number.");
                return false;
            }
    
            return selectedAccount.withdraw(amount);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }
    }
    
    public boolean transfer(LineReader reader, String targetAccountNumber, double amount) throws IOException {
        if (!ValidationUtils.isValidAmount(amount)) {
            System.out.println("Invalid amount must be greater than $0 and less than $10,000.");
            return false;
        }
    
        System.out.println("Choose one of your account(s) to transfer from:");
        displayAccountSelection();
    
        try {
            int selectedIndex = Integer.parseInt(reader.readLine().trim());
            Account currentAccount = getAccountByIndex(selectedIndex);
            
            if (currentAccount == null) {
                System.out.println("Invalid index number.");
                return false;
            }
            
            if (currentAccount.getBalance() < amount) {
                System.out.println("Insufficient balance.");
                return false;
            }
    
            DataAccess dataAccess = new DataAccess();
            ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
            Account targetAccount = getAccountByNumber(allAccounts, targetAccountNumber);
            
            if (targetAccount == null) {
                System.out.println("Invalid target account number.");
                return false;
            }
    
            return currentAccount.transfer(targetAccount, amount);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }
    }
    
    // Helper methods extracted for DRY principle
    private void displayAccountSelection() {
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00;-$#,##0.00");
        String separator = "═══════════════════════════════════════════";
    
        System.out.println("Index       Account Number      Balance");
        System.out.println(separator);
    
        int index = 1;
        double totalBalance = 0.0;
    
        for (Account acc : accounts) {
            totalBalance += acc.getBalance();
            String balanceString = currencyFormat.format(acc.getBalance());
            System.out.printf("%-5d %-15s %18s%n", index, acc.getAccountNumber(), balanceString);
            index++;
        }
    
        System.out.println(separator);
        System.out.printf("[%d accounts]   Total Balance: %s%n", accounts.size(), currencyFormat.format(totalBalance));
    }
    
    private Account getAccountByIndex(int index) {
        if (index < 1 || index > accounts.size()) return null;
        return accounts.get(index - 1);
    }
    
    private Account getAccountByNumber(List<Account> accounts, String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void createAccount() {
        DataAccess dataAccess = new DataAccess();
        Account newAccount = new Account(this.getNationalID());
        accounts.add(newAccount);
        boolean addNewAccount = dataAccess.addNewAccount(newAccount);
        if(addNewAccount){
            System.out.println("Account created succssefuly.");
            newAccount.displayInformations();
        }
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}