package com.banking.data;
import com.banking.config.Config;
import com.banking.core.*;
import com.banking.auth.*;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.text.*;
public class DataAccess {
    // This class is responsible for data access and storage.
    // It will handle reading and writing data to files or databases.
    
    /**
     * Loads all users from the CSV file into an ArrayList.
     * CSV Format: nationalID,firstName,fatherName,lastName,dateOfBirth,phoneNumber,userID,userName,password,userType,status
     * @return ArrayList<User> containing all users from the CSV file
     * @throws IOException if file reading fails
     * @throws ParseException if date parsing fails
     */
    public ArrayList<User> loadAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        
        
        try (CSVReader reader = new CSVReader(new FileReader(Config.USERS_FILE))) {
            // Skip header row
            reader.readNext();
            
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    // Parse user data from CSV columns
                    String nationalID = nextLine[0];
                    String fullName = String.format("%s %s %s", nextLine[1], nextLine[2], nextLine[3]);
                    
                    // Parse date with specific format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dateFormat.setLenient(false);
                    Date dateOfBirth = dateFormat.parse(nextLine[4]);
                    
                    String phoneNumber = nextLine[5];
                    String userID = nextLine[6];
                    String userName = nextLine[7];
                    String userPassword = nextLine[8];
                    
                    // Parse user role
                    UserRole userType = UserRole.valueOf(nextLine[9].toUpperCase());
                    String status = nextLine[10];

                    // Create and add user to list
                    User user = new User(nationalID, fullName, dateOfBirth, phoneNumber, 
                                       userID, userName, userPassword, userType, status);
                    users.add(user);
                    
                } 
                catch (ParseException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                    continue;
                } 
                catch (IllegalArgumentException e) {
                    System.err.println("Error parsing user role: " + e.getMessage());
                    continue;
                }
            }
        } 
        catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } 
        catch (CsvValidationException e) {
            System.err.println("Error validating CSV: " + e.getMessage());
        }
        
        return users;
    }

    /**
     * Saves all users to the CSV file.
     * @param users ArrayList<User> containing all users to save
     * @throws IOException if file writing fails
     */
    public void saveAllUsers(ArrayList<User> users) {
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(Config.USERS_FILE))) {
            // Write header
            String[] header = {"nationalID", "firstName", "fatherName", "lastName", 
                               "dateOfBirth", "phoneNumber", "userID", "userName", 
                               "password", "userType", "status"};
            writer.writeNext(header);
            
            // Write user data
            for (User user : users) {
                String[] userData = {
                    user.getNationalID(),
                    user.getFirstName(),
                    user.getFatherName(),
                    user.getLastName(),
                    new SimpleDateFormat("dd-MM-yyyy").format(user.getDateOfBirth()),
                    user.getPhoneNumber(),
                    user.getUserID(),
                    user.getUserName(),
                    user.getPassword(),
                    user.getUserType().toString(),
                    user.getStatus()
                };
                writer.writeNext(userData);
            }
        } 
        catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Loads all accounts from the CSV file into an ArrayList.
     * CSV Format: accountNumber,userID,balance,accountStatus,creationDate
     * @return ArrayList<Account> containing all accounts from the CSV file
     * @throws IOException if file reading fails
     * @throws ParseException if date parsing fails
     */
    public ArrayList<Account> loadAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Config.ACCOUNTS_FILE))) {
            // Skip header row
            reader.readNext();
            
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Parse account data from CSV columns
                try {
                    String accountNumber = nextLine[0];
                    String userID = nextLine[1];
                    Double balance = Double.parseDouble(nextLine[2]);
                    String status = nextLine[3];
                    AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
                    Date creationDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(nextLine[4]);
                    
                    // Create and add account to list
                    Account account = new Account(accountNumber, userID, balance, accountStatus, creationDate);
                    accounts.add(account);
                } catch (ParseException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                    continue;
                }
            }
        } 
        catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } 
        catch (CsvValidationException e) {
            System.err.println("Error validating CSV: " + e.getMessage());
        }
        
        return accounts;
    }

    /**
     * Saves all accounts to the CSV file.
     * @param accounts ArrayList<Account> containing all accounts to save
     * @throws IOException if file writing fails
     */
    public void saveAllAccounts(ArrayList<Account> accounts) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Config.ACCOUNTS_FILE))) {
            // Write header
            String[] header = {"accountNumber", "userID", "balance", "accountStatus", "creationDate"};
            writer.writeNext(header);
            
            // Write account data
            for (Account account : accounts) {
                String[] accountData = {
                    account.getAccountNumber(),
                    account.getUserID(),
                    String.valueOf(account.getBalance()),
                    account.getAccountStatus(),
                    new SimpleDateFormat("dd-MM-yyyy").format(account.getCreationDate())
                };
                writer.writeNext(accountData);
            }
        } 
        catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Adds a new user to the CSV file
     * @param newUser The user to add
     * @return boolean indicating if the operation was successful
     */
    public boolean addNewUser(User newUser) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Config.USERS_FILE, true))) { // true for append mode
            String[] userData = {
                newUser.getNationalID(),
                newUser.getFirstName(),
                newUser.getFatherName(),
                newUser.getLastName(),
                new SimpleDateFormat("dd-MM-yyyy").format(newUser.getDateOfBirth()),
                newUser.getPhoneNumber(),
                newUser.getUserID(),
                newUser.getUserName(),
                newUser.getPassword(),
                newUser.getUserType().toString(),
                newUser.getStatus()
            };
            writer.writeNext(userData);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
            return false;
        }
    }

    public boolean addNewAccount(Account newAccount) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Config.ACCOUNTS_FILE, true))) { // true for append mode
            String[] accountData = {
                newAccount.getAccountNumber(),
                newAccount.getUserID(),
                String.valueOf(newAccount.getBalance()),
                newAccount.getAccountStatus().toString(),
                newAccount.getCreationDate().toString()
            };
            writer.writeNext(accountData);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
            return false;
        }
    }
}