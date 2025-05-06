package com.banking.data;
import com.banking.config.Config;
import com.banking.core.*;
import com.banking.auth.*;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.util.*;
import java.io.*;
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
    public void loadAllUsers(ArrayList<User> users) {
        
        try (CSVReader reader = new CSVReader(new FileReader(Config.USERS_FILE))) {
            // Skip header row
            reader.readNext();
            
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    // Parse user data from CSV columns
                    String nationalID = nextLine[0];
                    String firstName = nextLine[1];
                    String fatherName = nextLine[2];
                    String familyName = nextLine[3];

                    // Parse date with specific format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dateFormat.setLenient(false);
                    Date dateOfBirth = dateFormat.parse(nextLine[4]);
                    
                    String phoneNumber = nextLine[5];
                    String userName = nextLine[6];
                    String userPassword = nextLine[7];
                    
                    // Parse user role
                    UserRole userType = UserRole.valueOf(nextLine[8].toUpperCase());
                    boolean status;
                    if(nextLine[9].equalsIgnoreCase("active")) {
                        status = true;
                    } 
                    else{
                            if (nextLine[10].equalsIgnoreCase("inactive")) {
                            status = false;
                            } 
                            else {
                                throw new IllegalArgumentException("Invalid user status: " + nextLine[10]);
                            }
                    }
                    User user = new User(nationalID, firstName, fatherName, familyName, dateOfBirth, phoneNumber,
                            userName, userPassword, userType, status);
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
                               "dateOfBirth", "phoneNumber", "userName", 
                               "password", "userType", "status"};
            writer.writeNext(header);
            
            // Write user data
            for (User user : users) {
                String[] userData = {
                    user.getNationalID().trim(),
                    user.getFirstName().trim(),
                    user.getFatherName().trim(),
                    user.getFamilyName(),
                    new SimpleDateFormat("dd-MM-yyyy").format(user.getDateOfBirth()),
                    user.getPhone().trim(),
                    user.getUsername().trim(),
                    user.getPassword().trim(),
                    user.getUserType().toString().trim(),
                    user.getStatus() ? "active" : "inactive"
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
    public ArrayList<Account> loadAllAccounts() throws IOException {
        ArrayList<Account> accounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Config.ACCOUNTS_FILE))) {
            // Skip header row
            reader.readNext();
    
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String accountNumber = nextLine[0].trim();
                String nationalID = nextLine[1].trim();
                Double balance = Double.parseDouble(nextLine[2]);
                String status = nextLine[3].trim();
                AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
                String creationDate = nextLine[4].trim(); // Directly use String
                
                // Create and add account to list
                Account account = new Account(accountNumber, nationalID, balance, accountStatus, creationDate);
                accounts.add(account);
            }
        } 
        catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            throw e; // Rethrow to maintain exception contract
        } 
        catch (CsvValidationException e) {
            System.err.println("Error validating CSV: " + e.getMessage());
            throw new IOException("CSV validation failed", e); // Wrap as IOException
        }
        
        return accounts;
    }

    /**
     * Filters users by type and returns a list of users of the specified type.
     * @param users ArrayList<User> containing all users
     * @param userType UserRole to filter by
     * @param userClass Class<T> to filter by
     * @return ArrayList<T> containing users of the specified type
     */
    public <T extends User> ArrayList<T> filterUsersByType(ArrayList<User> users, UserRole userType, Class<T> userClass) {
        ArrayList<T> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == userType && userClass.isInstance(user)) {
                filteredUsers.add(userClass.cast(user));
            }
        }
        return filteredUsers;
    }

    /**
     * Saves all accounts to the CSV file.
     * @param accounts ArrayList<Account> containing all accounts to save
     * @throws IOException if file writing fails
     */
    public void saveAllAccounts(ArrayList<Account> accounts) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Config.ACCOUNTS_FILE))) {
            // Write header
            String[] header = {"Account Number", "National ID", "Balance", "Account Status", "Creation Date"};
            writer.writeNext(header);
            // Write account data
            for (Account account : accounts) {
                String[] accountData = {
                    account.getAccountNumber().trim(),
                    account.getNationalID().trim(),
                    String.valueOf(account.getBalance()).trim(),
                    account.getAccountStatus().toString(),
                    account.getCreationDate()
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
                newUser.getNationalID().trim(),
                newUser.getFirstName().trim(),
                newUser.getFatherName().trim(),
                newUser.getFamilyName().trim(),
                new SimpleDateFormat("dd-MM-yyyy").format(newUser.getDateOfBirth()),
                newUser.getPhone().trim(),
                newUser.getUsername().trim(),
                newUser.getPassword().trim(),
                newUser.getUserType().toString().trim(),
                newUser.getStatus() ? "active" : "inactive"
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
                newAccount.getAccountNumber().trim(),
                newAccount.getNationalID().trim(),
                String.valueOf(newAccount.getBalance()).trim(),
                newAccount.getAccountStatus().toString().trim(),
                newAccount.getCreationDate().toString().trim()
            };
            writer.writeNext(accountData);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
            return false;
        }
    }
    /**
     * Updates the account balance in the CSV file
     * @param accountNumber The account number to update
     * @param amount The amount to add or subtract
     * @return boolean indicating if the operation was successful
     * @throws IOException 
     */
    public void updateAccountBalance(String accountNumber, double amount) throws IOException {
        ArrayList<Account> accounts = loadAllAccounts();
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                account.setBalance(account.getBalance() + amount);
                break;
            }
        }
        saveAllAccounts(accounts);
    }

    public ArrayList<Account> loadAllAccountsByUserID(String nationalID){
        ArrayList<Account> accounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Config.ACCOUNTS_FILE))) {
            // Skip header row
            reader.readNext();
            
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Parse account data from CSV columns
                if(nextLine[1].equals(nationalID)){
                    // Create and add account to list
                    Account account = new Account(nextLine[0], nextLine[1], Double.parseDouble(nextLine[2]), 
                    AccountStatus.valueOf(nextLine[3].toUpperCase()), nextLine[4]);
                    accounts.add(account);
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
}