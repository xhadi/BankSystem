package com.banking.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.banking.core.Account;
import com.banking.core.AccountStatus;
import com.banking.utils.ValidationUtils;

public class Admin extends User {

    public Admin(User user){
        super(user.getNationalID(), user.getFirstName(), user.getFatherName(), user.getFamilyName(), user.getDateOfBirth(), user.getPhone(),
        user.getUsername(), user.getPassword(), user.getUserType(), user.getStatus());
    }

    public Admin(String nationalID, String fullName, java.util.Date dateOfBirth, String phone,
                 String username, String password, UserRole userType, Boolean status) {
        super(nationalID, fullName, dateOfBirth, phone, username, password, userType, status);
    }
    
    public Admin(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
                String username, String password, UserRole userType, boolean status){
        super(nationalID, firstName, fatherName, familyName, dateOfBirth, phone,
                username, password, userType, status);
    }

    /**
     * Search for all accounts associated with a national ID
     * @param nationalID The national ID to search for
     * @param allAccounts List of all accounts
     * @return List of accounts belonging to the user, empty list if none found
     */
    public ArrayList<Account> searchAccounts(String nationalID, ArrayList<Account> allAccounts) {
        return allAccounts.stream()
                .filter(account -> account.getNationalID().equals(nationalID))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Search for a user by national ID
     * @param nationalID The national ID to search for
     * @param allUsers List of all users
     * @return User object if found, null otherwise
     */
    public <T extends User> T searchUser(String nationalID, ArrayList<T> allUsers) {
        return allUsers.stream()
                .filter(user -> user.getNationalID().equals(nationalID))
                .findFirst()
                .orElse(null);
    }

    public boolean updateAccountDetails(String accountNumber, Account updatedAccount, List<Account> allAccounts) {
        for (int i = 0; i < allAccounts.size(); i++) {
            if (allAccounts.get(i).getAccountNumber().equals(accountNumber)) {
                allAccounts.set(i, updatedAccount);
                return true;
            }
        }
        return false;
    }

    public void activateUserAccount(String accountNumber, ArrayList<Account> allAccounts) {
        allAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .ifPresentOrElse(
                    account -> {
                        account.setStatus(AccountStatus.ACTIVE);
                        System.out.println("Account activated successfully!");
                    },
                    () -> System.out.println("Account not found.")
                );
    }

    public void deactivateUserAccount(String accountNumber, List<Account> allAccounts) {
        allAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .ifPresentOrElse(
                    account -> {
                        account.setStatus(AccountStatus.INACTIVE);
                        System.out.println("Account inactivated successfully!");
                    },
                    () -> System.out.println("Account not found.")
                );
    }

    public void activateUser(String nationalID, ArrayList<User> existingUsers) {
        existingUsers.stream()
                .filter(user -> user.getNationalID().equals(nationalID))
                .findFirst()
                .ifPresentOrElse(
                    user -> {
                        user.setStatus(true);
                        System.out.println("User activated successfully!");
                    },
                    () -> System.out.println("User not found.")
                );
    }

    public void deActivateUser(String nationalID, ArrayList<User> existingUsers) {
        existingUsers.stream()
                .filter(user -> user.getNationalID().equals(nationalID))
                .findFirst()
                .ifPresentOrElse(
                    user -> {
                        user.setStatus(false);
                        System.out.println("User activated successfully!");
                    },
                    () -> System.out.println("User not found.")
                );
    }

    public <T extends User> boolean resetUserPassword(String nationalID, ArrayList<T> existingUsers, String newPassword) {
        
        if (!ValidationUtils.isValidPassword(newPassword)) {
            return false;
        }
        boolean userFound = false;
        for (T user : existingUsers) {
            if (user.getNationalID().equals(nationalID)) {
                user.setPassword(newPassword);
                userFound = true;
                break;
            }
        }
        if (userFound) {
            return true;
        } 
        else {
            System.out.println("User not found.");
            return false;
        }
    }
}