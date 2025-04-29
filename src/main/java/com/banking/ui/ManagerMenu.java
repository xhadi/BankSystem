package com.banking.ui;
import java.util.Scanner;
public class ManagerMenu {
    public void displayManagerMenu() {
        System.out.println("Welcome to the Manager Menu");
        System.out.println("1. View All Users");
        System.out.println("2. Search for User");
        System.out.println("3. Manage User Accounts");
        System.out.println("4. Logout");
        System.out.print("Please select an option: ");
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

    public void handleManagerMenu() {
        char input;
        // This method will handle manager input and call the appropriate methods
        do{
            displayManagerMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view all users
                    break;
                case '2':
                    // Call method to search for user
                    break;
                case '3':
                    manageUserAccounts();
                    break;
                case '4':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }while(input != '4');
    }
}
