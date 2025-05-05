package com.banking.ui;
import java.util.Scanner;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

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
    private Scanner scanner;

    public ManagerMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayManagerMenu() {
        System.out.println("\n========== Manager Menu ==========");
        System.out.println("1. View All Users");
        System.out.println("2. Search for User");
        System.out.println("3. Manage User Account");
        System.out.println("4. Logout");
        System.out.print("Please select an option: ");
    }

    public void manageUserAccounts(Manager manager, ArrayList<EndUser> existingEndUsers, DataAccess dataAccess) throws IOException {
        char input;
        do {
            System.out.println("\n========== Manage User Accounts ==========");
            System.out.println("1. View User Account");
            System.out.println("2. Edit Users");
            System.out.println("3. Activate/Deactivate User Account");
            System.out.println("4. Reset User Password");
            System.out.println("5. Back to Manager Menu");
            System.out.print("Please select an option: ");
            input = scanner.nextLine().charAt(0);

            switch (input) {
                case '1':
                    viewUserAccount(manager, dataAccess);
                    break;
                case '2':
                    editUserAccount(manager, existingEndUsers, dataAccess);
                    break;
                case '3':
                    viewUserAccount(manager, dataAccess);
                    System.out.println("1. Activate     2. Deactivate");
                    input = scanner.nextLine().charAt(0);
                    String accountNumber;
                    boolean success;
                    switch (input) {
                        case '1':
                            System.out.print("Enter the account number: ");
                            accountNumber = scanner.nextLine();
                            success = ValidationUtils.isValidNationalID(accountNumber);
                            if(!success){
                                System.out.println("Invalid National ID.");
                            }
                            else{
                                ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
                                activateUserAccount(accountNumber, allAccounts);
                            }
                            break;
                        case '2':
                            System.out.print("Enter the account number: ");
                            accountNumber = scanner.nextLine();
                            success = ValidationUtils.isValidNationalID(accountNumber);
                            if(!success){
                                System.out.println("Invalid National ID.");
                            }
                            else{
                                ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
                                activateUserAccount(accountNumber, allAccounts);
                            }
                            break;
                        default:
                            System.out.println("Invalid choice."); 
                        }
                    break;
                case '4':
                    resetUserPassword(manager, existingEndUsers, dataAccess);
                    break;
                case '5':
                    System.out.println("Returning to Manager Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input != '5');
    }

    private void viewUserAccount(Manager manager, DataAccess dataAccess) {
        System.out.print("Enter user's national ID: ");
        String nationalID = scanner.nextLine();
        ArrayList<Account> allAccounts = dataAccess.loadAllAccounts();
        ArrayList<Account> userAccounts = manager.searchAccounts(nationalID, allAccounts);

        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found for this user.");
        } else {
            System.out.println("\n--- User Accounts ---");
            userAccounts.forEach(Account::displayInformations);
        }
    }

    private void editUserAccount(Manager manager, ArrayList<EndUser> existingEndUsers, DataAccess dataAccess) {
        System.out.print("Enter user's national ID: ");
        String nationalID = scanner.nextLine();
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

    private void resetUserPassword(Manager manager, ArrayList<EndUser> existingEndUsers, DataAccess dataAccess) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        
        System.out.print("Enter user's national ID: ");
        String nationalID = scanner.nextLine();
        String newPassword = reader.readLine("Enter new password: ", '*');

        manager.resetUserPassword(nationalID, existingEndUsers, newPassword);
        dataAccess.saveAllUsers(new ArrayList<>(existingEndUsers));
    }

    public void handleManagerMenu(Manager managerUser, ArrayList<EndUser> existingEndUsers, DataAccess dataAccess) throws IOException {
        char input;
        do {
            displayManagerMenu();
            input = scanner.nextLine().charAt(0);
            switch (input) {
                case '1':
                    System.out.println("\n--- All Users ---");
                    existingEndUsers.forEach(User::viewPersonalInfo);
                    break;
                case '2':
                    System.out.print("Enter national ID to search: ");
                    String nationalID = scanner.nextLine();
                    User user = managerUser.searchUser(nationalID, existingEndUsers);
                    if (user != null) {
                        user.viewPersonalInfo();
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case '3':
                    manageUserAccounts(managerUser, existingEndUsers, dataAccess);
                    break;
                case '4':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input != '4');
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
}