package com.banking.ui;

import java.util.Scanner;

public class UserMenu {
    public void displayUserMenu() {
        System.out.println("Welcome to the User Menu");
        System.out.println("1. View Account Details");
        System.out.println("2. Reset Password");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Money");
        System.out.println("6. Logout");
        System.out.print("Please select an option: ");
    }

    public void handleUserMenu() {
        char input;
        // This method will handle user input and call the appropriate methods
        do{
            displayUserMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();
            switch (input) {
                case '1':
                    // Call method to view account details
                    break;
                case '2':
                    // Call method to deposit money
                    break;
                case '3':
                    // Call method to withdraw money
                    break;
                case '4':
                    // Call method to transfer money
                    break;
                case '5':
                    // Call method to reset password
                    break;
                case '6':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }while(input != '6');
    }
}
