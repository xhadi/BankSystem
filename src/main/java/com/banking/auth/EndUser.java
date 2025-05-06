package com.banking.auth;

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
            System.out.println("Deposit amount must be at least $10 or in increments of $10 and less than $10,000.");
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

    public boolean withdrawal(double amount){
        if (!ValidationUtils.isValidAmount(amount) || amount % 10 != 0) {
            System.out.println("Invalid amount must $10 or in increments of $10 and less than $10,000.");
        }

        System.out.println("Chose one of your account(s) to withdraw from it");

        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00;-$#,##0.00");
        String separator = "═══════════════════════════════════════════";

        // Header
        System.out.println("Index       Account Number      Balance");
        System.out.println(separator);

        int index = 1;
        double totalBalance = 0.0;

        // Account Rows
        for (Account acc : accounts) {
            totalBalance += acc.getBalance();
            String balanceString = currencyFormat.format(acc.getBalance());

            System.out.printf("%-5d %-15s %18s%s%n",
                    index,
                    acc.getAccountNumber(),
                    balanceString);

            index++;
        }

        // Footer
        System.out.println(separator);
        System.out.printf("[%d accounts]   Total Balance: %s%n",
                accounts.size(),
                currencyFormat.format(totalBalance));

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the index of the account: ");
        int selectedIndex = input.nextInt();
        input.close();
        Account selectedAccount = null;
        boolean found = false;
        for (Account acc : accounts) {
            if (selectedIndex == accounts.indexOf(acc) + 1) {
                selectedAccount = acc;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid index number.");
            return false;
        }

        boolean success = selectedAccount.withdraw(amount);
        if (!success) {
            System.out.println("Withdrawal failed. Insufficient funds.");
            return false;
        }

        return true;
    }

    public boolean transfer(String targetAccountNumber, double amount){
        if(!ValidationUtils.isValidAmount(amount)){
            System.out.println("Invalid amount must be grater than $0 and less than $10,000.");
            return false;
        }

        System.out.println("Chose one of your account(s) to transfer from it");

        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00;-$#,##0.00");
        String separator = "═══════════════════════════════════════════";

        // Header
        System.out.println("Index       Account Number      Balance");
        System.out.println(separator);

        int index = 1;
        double totalBalance = 0.0;

        // Account Rows
        for (Account acc : accounts) {
            totalBalance += acc.getBalance();
            String balanceString = currencyFormat.format(acc.getBalance());

            System.out.printf("%-5d %-15s %18s%s%n",
                    index,
                    acc.getAccountNumber(),
                    balanceString);

            index++;
        }

        // Footer
        System.out.println(separator);
        System.out.printf("[%d accounts]   Total Balance: %s%n",
                accounts.size(),
                currencyFormat.format(totalBalance));

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the index of the account: ");
        int selectedIndex = input.nextInt();
        input.close();
        Account currentAccount = null;
        boolean foundCurrent = false;
        for (Account acc : accounts) {
            if (selectedIndex == accounts.indexOf(acc) + 1) {
                currentAccount = acc;
                foundCurrent = true;
                break;
            }
        }

        if (!foundCurrent) {
            System.out.println("Invalid index number.");
            return false;
        }
        else{
            if(currentAccount.getBalance() < amount){
               System.out.println("Insufficient balance.");
               return false; 
            }
        }
        
        DataAccess dataAccess = new DataAccess();
        ArrayList<Account> accounts = dataAccess.loadAllAccounts();
        Account targetAccount = null;
        boolean found = false;
        for(Account acc : accounts){
            if(acc.getAccountNumber() == targetAccountNumber){
                targetAccount = acc;
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("Invalid Account number.");
            return false;
        }

        boolean success = currentAccount.transfer(targetAccount, amount);
        if(!success){
            System.out.println("Targeted account is not active");

        }
        
        return true;
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