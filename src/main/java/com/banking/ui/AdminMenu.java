package com.banking.ui;
import java.util.Scanner;

import com.banking.utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import com.banking.auth.*;
import com.banking.data.DataAccess;
public class AdminMenu {
    public void displayAdminMenu() {
        System.out.println("1. View All Users");
        System.out.println("2. View All Managers");
        System.out.println("3. Search for User or Manager by National ID");
        System.out.println("4. Manage User Accounts");
        System.out.println("5. Manage Manager Accounts");
        System.out.println("6. Edit Your Personal Information");
        System.out.println("7. Logout");
        System.out.print("Please select an option: ");
    }

    public void handleAdminMenu(Admin adminUser,ArrayList<User> exsistingAllUsers, DataAccess dataAccess) throws IOException {
        ArrayList<Manager> exsistingManagers = dataAccess.filterUsersByType(exsistingAllUsers,UserRole.MANAGER,Manager.class);
        ArrayList<EndUser> exsistingEndUsers = dataAccess.filterUsersByType(exsistingAllUsers,UserRole.ENDUSER,EndUser.class);
        
        char input;
        // This method will handle admin input and call the appropriate methods
        System.out.println("Welcome " + adminUser.getFirstName() + " in the Admin Menu");
        do{
            displayAdminMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view all users
                    adminUser.viewUsersInBatches(exsistingEndUsers, "Users");
                    break;
                case '2':
                    // Call method to view all managers
                    adminUser.viewUsersInBatches(exsistingManagers, "Managers");
                    break;
                case '3':
                    // Call method to search for user or manager
                    System.out.print("Enter National ID to search: ");
                    Scanner scanner1 = new Scanner(System.in);
                    String nationalID = scanner1.nextLine();
                    boolean isValid = ValidationUtils.isValidNationalID(nationalID);
                    scanner1.close();
                    if(!isValid){
                        System.out.println("Invalid national ID. Please try again.");
                        break;
                    }
                    User user = adminUser.searchUser(nationalID, exsistingAllUsers);
                    if(user == null || user.getUserType() == UserRole.ADMIN){
                        System.out.println("User not found.");
                        break;
                    }
                    System.out.println("founded loading information...");
                    user.viewPersonalInfo();
                    break;
                case '4':
                    manageUserAccounts();
                    break;
                case '5':
                    manageManagerAccounts();
                    break;
                case '6':
                    adminUser.editPersonalInformationb(dataAccess,exsistingAllUsers);
                    break;
                case '7':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }while(input != '7');
    }

    public void manageUserAccounts() {
        char input;
        // This method will handle user account management
        do{
            System.out.println("1. View User Account by National ID");
            System.out.println("2. Edit User Accounts by National ID");
            System.out.println("3. Delete User Accounts by National ID");
            System.out.println("4. Reset User Password by National ID");
            System.out.println("5. Back to Main Menu");
            System.out.print("Please select an option: ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view user accounts
                    break;
                case '2':
                    // Call method to edit user accounts
                    break;
                case '3':
                    // Call method to delete user accounts
                    break;
                case '4':
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }while(input != '5');
    }

    public void manageManagerAccounts() {
        char input;
        // This method will handle manager account management
        do{
            System.out.println("1. View Manager Account by National ID");
            System.out.println("2. Edit Manager Accounts by National ID");
            System.out.println("3. Delete Manager Accounts by National ID");
            System.out.println("4. Reset Manager Password by National ID");
            System.out.println("5. Back to Main Menu");
            System.out.print("Please select an option: ");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view manager accounts
                    break;
                case '2':
                    // Call method to edit manager accounts
                    break;
                case '3':
                    // Call method to delete manager accounts
                    break;
                case '4':
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }while(input != '5');
    }
}