package com.banking.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.banking.core.*;
import com.banking.utils.ValidationUtils;

public class Manager extends User {

    public Manager(String nationalID, String fullName, Date dateOfBirth, String phone,
                   String username, String password, UserRole userType, Boolean status) {
        super(nationalID, fullName, dateOfBirth, phone, username, password, userType, status);
    }

    public Manager(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
                String username, String password, UserRole userType, boolean status){
        super(nationalID, firstName, fatherName, familyName, dateOfBirth, phone,
                username, password, userType, status);
    }

    /**
     * Create an account for a user
     * @param nationalID The national ID of the user
     * @param existingUsers List of existing users
     * @return true if the account was created successfully, false otherwise
     */
    public boolean createAccountToUser(String nationalID, ArrayList<EndUser> existingUsers) {
        EndUser selectedUser = existingUsers.stream()
                .filter(user -> user.getNationalID().equals(nationalID))
                .findFirst()
                .orElse(null);
        
        if (selectedUser == null) {
            System.out.println("User not found.");
            return false;
        }

        selectedUser.createAccount();
        return true;
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

    public User searchUser(String nationalID, ArrayList<EndUser> endUsers) {
        return endUsers.stream()
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
                        account.setStatus(AccountStatus.Active);
                        System.out.println("Account activated successfully!");
                    },
                    () -> System.out.println("Account not found.")
                );
    }

    public void deactivateUserAccount(String accountNumber, ArrayList<Account> allAccounts) {
        allAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .ifPresentOrElse(
                    account -> {
                        account.setStatus(AccountStatus.Inactive);
                        System.out.println("Account inactivated successfully!");
                    },
                    () -> System.out.println("Account not found.")
                );
    }

    public void resetUserPassword(String nationalID, ArrayList<EndUser> existingUsers, String newPassword) {
        
        if (!ValidationUtils.isValidPassword(newPassword)) {
            System.out.println("The password invalid, it must be at least 8 characters and include an uppercase letter, a number, and a special character.");
            return;
        }
        existingUsers.stream()
                .filter(user -> user.getNationalID().equals(nationalID))
                .findFirst()
                .ifPresentOrElse(
                    user -> {
                        user.setPassword(newPassword);
                        System.out.println("Password reset successfully!");
                    },
                    () -> System.out.println("User not found.")
                );
    }
}