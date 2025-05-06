package com.banking.ui;
import org.jline.reader.LineReader;

import com.banking.utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import com.banking.auth.*;
import com.banking.data.DataAccess;

public class AdminMenu {
    public void displayAdminMenu() {
        System.out.println("\nAdmin Menu Options:");
        System.out.println("1. View All Users");
        System.out.println("2. View All Managers");
        System.out.println("3. Search for User or Manager by National ID");
        System.out.println("4. Manage User Accounts");
        System.out.println("5. Manage Manager Accounts");
        System.out.println("6. Edit Your Personal Information");
        System.out.println("7. Logout");
    }

    public void handleAdminMenu(Admin adminUser, ArrayList<User> existingAllUsers, DataAccess dataAccess, LineReader reader) throws IOException {
        ArrayList<Manager> existingManagers = dataAccess.filterUsersByType(existingAllUsers, UserRole.MANAGER, Manager.class);
        ArrayList<EndUser> existingEndUsers = dataAccess.filterUsersByType(existingAllUsers, UserRole.ENDUSER, EndUser.class);
        
        String input;
        System.out.println("\nWelcome " + adminUser.getFirstName() + " to the Admin Menu");
        do {
            displayAdminMenu();
            input = reader.readLine("Please select an option: ").trim();
            
            switch (input.charAt(0)) {
                case '1':
                    adminUser.viewUsersInBatches(existingEndUsers, "Users");
                    break;
                case '2':
                    adminUser.viewUsersInBatches(existingManagers, "Managers");
                    break;
                case '3':
                    String nationalID = reader.readLine("Enter National ID to search: ").trim();
                    boolean isValid = ValidationUtils.isValidNationalID(nationalID);
                    if (!isValid) {
                        System.out.println("Invalid national ID. Please try again.");
                        break;
                    }
                    User user = adminUser.searchUser(nationalID, existingAllUsers);
                    if (user == null || user.getUserType() == UserRole.ADMIN) {
                        System.out.println("User not found.");
                        break;
                    }
                    System.out.println("Found user. Loading information...");
                    user.viewPersonalInfo();
                    break;
                case '4':
                    manageUserAccounts(reader);
                    break;
                case '5':
                    manageManagerAccounts(reader);
                    break;
                case '6':
                    adminUser.editPersonalInformation(dataAccess, existingAllUsers);
                    break;
                case '7':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '7');
    }

    public void manageUserAccounts(LineReader reader) {
        String input;
        do {
            System.out.println("\nUser Account Management:");
            System.out.println("1. View User Account by National ID");
            System.out.println("2. Edit User Accounts by National ID");
            System.out.println("3. Delete User Accounts by National ID");
            System.out.println("4. Reset User Password by National ID");
            System.out.println("5. Back to Main Menu");
            
            input = reader.readLine("Please select an option: ").trim();
            
            switch (input.charAt(0)) {
                case '1':
                    // Implement view user accounts
                    break;
                case '2':
                    // Implement edit user accounts
                    break;
                case '3':
                    // Implement delete user accounts
                    break;
                case '4':
                    // Implement reset password
                    break;
                case '5':
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '5');
    }

    public void manageManagerAccounts(LineReader reader) {
        String input;
        do {
            System.out.println("\nManager Account Management:");
            System.out.println("1. View Manager Account by National ID");
            System.out.println("2. Edit Manager Accounts by National ID");
            System.out.println("3. Delete Manager Accounts by National ID");
            System.out.println("4. Reset Manager Password by National ID");
            System.out.println("5. Back to Main Menu");
            
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) continue;
            
            switch (input.charAt(0)) {
                case '1':
                    // Implement view manager accounts
                    break;
                case '2':
                    // Implement edit manager accounts
                    break;
                case '3':
                    // Implement delete manager accounts
                    break;
                case '4':
                    // Implement reset password
                    break;
                case '5':
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '5');
    }
}