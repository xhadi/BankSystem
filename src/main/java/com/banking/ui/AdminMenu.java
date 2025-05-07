package com.banking.ui;
import org.jline.reader.LineReader;

import com.banking.utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import com.banking.auth.*;
import com.banking.data.DataAccess;

public class AdminMenu {
    // Add member variables to store references
    private Admin adminUser;
    private ArrayList<User> existingAllUsers;
    private DataAccess dataAccess;
    private LineReader reader;
    private ArrayList<Manager> existingManagers;
    private ArrayList<EndUser> existingEndUsers;

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
        // Store all references as member variables
        this.adminUser = adminUser;
        this.existingAllUsers = existingAllUsers;
        this.dataAccess = dataAccess;
        this.reader = reader;
        this.existingManagers = dataAccess.filterUsersByType(existingAllUsers, UserRole.MANAGER, Manager.class);
        this.existingEndUsers = dataAccess.filterUsersByType(existingAllUsers, UserRole.ENDUSER, EndUser.class);
        
        String input;
        System.out.println("\nWelcome " + adminUser.getFirstName() + " to the Admin Menu");
        do {
            displayAdminMenu();
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }

            switch (input.charAt(0)) {
                case '1':
                    adminUser.viewUsersInBatches(this.existingEndUsers, "Users", reader);
                    break;
                case '2':
                    adminUser.viewUsersInBatches(existingManagers, "Managers", reader);
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
                    manageUserAccounts();  // No parameters needed - using member variables
                    break;
                case '5':
                    manageManagerAccounts();  // No parameters needed - using member variables
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

    // Updated to use member variables instead of parameters
    public void manageUserAccounts() {
        String input;
        do {
            System.out.println("\nUser Account Management:");
            System.out.println("1. View User Account by National ID");
            System.out.println("2. Edit User Accounts by National ID");
            System.out.println("3. Delete User Accounts by National ID");
            System.out.println("4. Reset User Password by National ID");
            System.out.println("5. Back to Main Menu");
            
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }
            
            switch (input.charAt(0)) {
                case '1':
                    // Implement view user accounts using member variables
                    String viewId = reader.readLine("Enter user's National ID: ").trim();
                    User viewUser = adminUser.searchUser(viewId, existingEndUsers);
                    if (viewUser != null) {
                        viewUser.viewPersonalInfo();
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case '2':
                    // Implement edit user accounts
                    String editId = reader.readLine("Enter user's National ID: ").trim();
                    User editUser = adminUser.searchUser(editId, existingEndUsers);
                    if (editUser != null) {
                        try {
                            editUser.editUserInformation(dataAccess, existingAllUsers);
                        } catch (IOException e) {
                            System.out.println("Error editing user: " + e.getMessage());
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case '3':
                    // Implement delete user accounts
                    String deleteId = reader.readLine("Enter user's National ID to delete: ").trim();
                    User deleteUser = adminUser.searchUser(deleteId, existingEndUsers);
                    if (deleteUser != null) {
                        existingAllUsers.remove(deleteUser);
                        existingEndUsers.remove(deleteUser);
                        dataAccess.saveAllUsers(existingAllUsers);
                        System.out.println("User deleted successfully.");
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case '4':
                    // Implement reset password
                    String resetId = reader.readLine("Enter user's National ID: ").trim();
                    String newPassword = reader.readLine("Enter new password: ").trim();
                    if (ValidationUtils.isValidPassword(newPassword)) {
                        boolean success = adminUser.resetUserPassword(resetId, existingEndUsers, newPassword);
                        if (success) {
                            dataAccess.saveAllUsers(existingAllUsers);
                            System.out.println("Password reset successfully.");
                        } else {
                            System.out.println("User not found.");
                        }
                    } else {
                        System.out.println("Invalid password format.");
                    }
                    break;
                case '5':
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '5');
    }

    // Updated to use member variables instead of parameters
    public void manageManagerAccounts() {
        String input;
        do {
            System.out.println("\nManager Account Management:");
            System.out.println("1. View Manager Account by National ID");
            System.out.println("2. Edit Manager Accounts by National ID");
            System.out.println("3. Delete Manager Accounts by National ID");
            System.out.println("4. Reset Manager Password by National ID");
            System.out.println("5. Back to Main Menu");
            
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }
            
            switch (input.charAt(0)) {
                case '1':
                    // Implement view manager accounts
                    String viewId = reader.readLine("Enter manager's National ID: ").trim();
                    User viewManager = adminUser.searchUser(viewId, existingManagers);
                    if (viewManager != null) {
                        viewManager.viewPersonalInfo();
                    } else {
                        System.out.println("Manager not found.");
                    }
                    break;
                case '2':
                    // Implement edit manager accounts
                    String editId = reader.readLine("Enter manager's National ID: ").trim();
                    User editManager = adminUser.searchUser(editId, existingManagers);
                    if (editManager != null) {
                        try {
                            editManager.editUserInformation(dataAccess, existingAllUsers);
                        } catch (IOException e) {
                            System.out.println("Error editing manager: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Manager not found.");
                    }
                    break;
                case '3':
                    // Implement delete manager accounts
                    String deleteId = reader.readLine("Enter manager's National ID to delete: ").trim();
                    User deleteManager = adminUser.searchUser(deleteId, existingManagers);
                    if (deleteManager != null) {
                        existingAllUsers.remove(deleteManager);
                        existingManagers.remove(deleteManager);
                        dataAccess.saveAllUsers(existingAllUsers);
                        System.out.println("Manager deleted successfully.");
                    } else {
                        System.out.println("Manager not found.");
                    }
                    break;
                case '4':
                    // Implement reset password
                    String resetId = reader.readLine("Enter manager's National ID: ").trim();
                    String newPassword = reader.readLine("Enter new password: ").trim();
                    if (ValidationUtils.isValidPassword(newPassword)) {
                        boolean success = adminUser.resetUserPassword(resetId, existingManagers, newPassword);
                        if (success) {
                            dataAccess.saveAllUsers(existingAllUsers);
                            System.out.println("Password reset successfully.");
                        } else {
                            System.out.println("Manager not found.");
                        }
                    } else {
                        System.out.println("Invalid password format.");
                    }
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