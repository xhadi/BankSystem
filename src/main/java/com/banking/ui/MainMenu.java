package com.banking.ui;

import java.util.Scanner;

public class MainMenu {
    public void handleMainMenu(){
        // Main menu for the System
        // It will display options to the user and handle their input
        char input;
        do {
            displayMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().charAt(0);
            scanner.close();

            switch (input) {
                case '1':
                    // Call method to create user
                    break;
                case '2':
                    // Call method to login
                    break;
                case '3':
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input != '3');
    }

    public void displayMenu() {
        System.out.println("Welcome to the Banking System");
        System.out.println("1. Create User");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Please select an option: ");
    }
}
