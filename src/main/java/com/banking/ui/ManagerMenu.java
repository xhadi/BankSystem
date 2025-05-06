package com.banking.ui;

import org.jline.reader.LineReader;

import java.io.IOException;
import java.util.ArrayList;
import com.banking.auth.Manager;
import com.banking.auth.EndUser;
import com.banking.auth.User;
import com.banking.data.DataAccess;
import com.banking.core.Account;
import com.banking.core.AccountStatus;
import com.banking.utils.ValidationUtils;

public class ManagerMenu {

    public void displayManagerMenu() {
        System.out.println("\n========== Manager Menu ==========");
        System.out.println("1. View All Users");
        System.out.println("2. Search for User");
        System.out.println("3. Manage User Account");
        System.out.println("4. Edit Your Personal Information");
        System.out.println("5. Logout");
    }

    public void manageUserAccounts(Manager manager, ArrayList<EndUser> existingEndUsers, 
                                 DataAccess dataAccess, LineReader reader) throws IOException {
        String input;
        do {
            System.out.println("\n========== Manage User Accounts ==========");
            System.out.println("1. View User Account");
            System.out.println("2. Edit Users");
            System.out.println("3. Activate/Deactivate User Account");
            System.out.println("4. Reset User Password");
            System.out.println("5. Back to Manager Menu");
            
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) continue;

            switch (input.charAt(0)) {
                case '1':
                    viewUserAccount(manager, dataAccess, reader);
                    break;
                case '2':
                    editUserAccount(manager, existingEndUsers, dataAccess, reader);
                    break;
                case '3':
                    handleAccountActivation(manager, dataAccess, reader);
                    break;
                case '4':
                    resetUserPassword(manager, existingEndUsers, dataAccess, reader);
                    break;
                case '5':
                    System.out.println("Returning to Manager Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '5');
    }

    private void viewUserAccount(Manager manager, DataAccess dataAccess, LineReader reader) throws IOException {
        String nationalID = reader.readLine("Enter user's national ID: ").trim();
        ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
        ArrayList<Account> userAccounts = manager.searchAccounts(nationalID, allAccounts);

        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found for this user.");
        } else {
            System.out.println("\n--- User Accounts ---");
            userAccounts.forEach(Account::displayInformations);
        }
    }

    private void editUserAccount(Manager manager, ArrayList<EndUser> existingEndUsers,
                                DataAccess dataAccess, LineReader reader) {
        String nationalID = reader.readLine("Enter user's national ID: ").trim();
        User user = manager.searchUser(nationalID, existingEndUsers);

        if (user != null) {
            try {
                user.editUserInformation(dataAccess, new ArrayList<>(existingEndUsers));
                dataAccess.saveAllUsers(new ArrayList<>(existingEndUsers));
            } catch (IOException e) {
                System.out.println("Error saving user data: " + e.getMessage());
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void resetUserPassword(Manager manager, ArrayList<EndUser> existingEndUsers,
                                  DataAccess dataAccess, LineReader reader) throws IOException {
        String nationalID = reader.readLine("Enter user's national ID: ").trim();
        String newPassword = reader.readLine("Enter new password: ", '*').trim();

        if (ValidationUtils.isValidPassword(newPassword)) {
            manager.resetUserPassword(nationalID, existingEndUsers, newPassword);
            dataAccess.saveAllUsers(new ArrayList<>(existingEndUsers));
            System.out.println("Password reset successfully.");
        } else {
            System.out.println("Invalid password. Must be 8+ characters with uppercase, lowercase, number, and special character.");
        }
    }

    public void handleManagerMenu(Manager managerUser, ArrayList<EndUser> existingEndUsers,
                                 DataAccess dataAccess, LineReader reader, ArrayList<User> existingUsers) throws IOException {
        String input;
        do {
            displayManagerMenu();
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) continue;

            switch (input.charAt(0)) {
                case '1':
                    System.out.println("\n--- All Users ---");
                    existingEndUsers.forEach(User::viewPersonalInfo);
                    break;
                case '2':
                    String nationalID = reader.readLine("Enter national ID to search: ").trim();
                    User user = managerUser.searchUser(nationalID, existingEndUsers);
                    if (user != null) {
                        user.viewPersonalInfo();
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case '3':
                    manageUserAccounts(managerUser, existingEndUsers, dataAccess, reader);
                    break;
                case '5':
                    System.out.println("Logging out...");
                    break;
                case '4':
                    managerUser.editPersonalInformation(dataAccess, existingUsers);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '5');
    }

    private void handleAccountActivation(Manager manager, DataAccess dataAccess, LineReader reader) throws IOException {
        String accountChoice = reader.readLine("1. Activate     2. Deactivate: ").trim();
        if (accountChoice.isEmpty()) return;

        String accountNumber = reader.readLine("Enter the account number: ").trim();
        if (!ValidationUtils.isValidAccountNumber(accountNumber)) {
            System.out.println("Invalid account number.");
            return;
        }

        ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
        switch (accountChoice.charAt(0)) {
            case '1':
                activateUserAccount(accountNumber, allAccounts);
                break;
            case '2':
                deactivateUserAccount(accountNumber, allAccounts);
                break;
            default:
                System.out.println("Invalid choice.");
        }
        dataAccess.saveAllAccounts(allAccounts);
    }

    private void activateUserAccount(String accountNumber, ArrayList<Account> allAccounts) {
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

    private void deactivateUserAccount(String accountNumber, ArrayList<Account> allAccounts) {
        allAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .ifPresentOrElse(
                    account -> {
                        account.setStatus(AccountStatus.INACTIVE);
                        System.out.println("Account deactivated successfully!");
                    },
                    () -> System.out.println("Account not found.")
                );
    }
}