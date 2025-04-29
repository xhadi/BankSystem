package com.banking.ui;
import java.util.Scanner;
public class AdminMenu {
    public void displayAdminMenu() {
        System.out.println("Welcome to the Admin Menu");
        System.out.println("1. View All Users");
        System.out.println("2. View All Managers");
        System.out.println("3. Search for User");
        System.out.println("4. Search for Manager");
        System.out.println("5. Manage User Accounts");
        System.out.println("6. Manage Manager Accounts");
        System.out.println("7. Logout");
        System.out.print("Please select an option: ");
    }

    public void handleAdminMenu() {
        char input;
        // This method will handle admin input and call the appropriate methods
        do{
            displayAdminMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view all users
                    break;
                case '2':
                    // Call method to view all managers
                    break;
                case '3':
                    // Call method to search for user
                    break;
                case '4':
                    // Call method to search for manager
                    break;
                case '5':
                    manageUserAccounts();
                    break;
                case '6':
                    manageManagerAccounts();
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
            System.out.println("1. View User Account by ID");
            System.out.println("2. Edit User Accounts by ID");
            System.out.println("3. Delete User Accounts by ID");
            System.out.println("4. Reset User Password by ID");
            System.out.println("5. Back to Manager Menu");
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
                    System.out.println("Returning to Manager Menu...");
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
            System.out.println("1. View Manager Account by ID");
            System.out.println("2. Edit Manager Accounts by ID");
            System.out.println("3. Delete Manager Accounts by ID");
            System.out.println("4. Reset Manager Password by ID");
            System.out.println("5. Back to Admin Menu");
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