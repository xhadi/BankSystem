package com.banking.ui;

import java.util.Scanner;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.banking.auth.*;
import com.banking.data.DataAccess;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MainMenu {
    public void handleMainMenu(DataAccess dataAccess,ArrayList<User> exsistingAllUsers) throws ParseException, IOException {
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
                    User newUser = User.crateUser(exsistingAllUsers);
                    if (newUser == null) {
                        System.out.println("Failed to create user. Please try again.");
                    } 
                    else {
                        boolean success = dataAccess.addNewUser(newUser);
                        if(!success){
                            System.out.println("Failed to save user data. Please try again.");
                            return;
                        }
                        exsistingAllUsers.add(newUser);
                        System.out.println("User created successfully.");
                        System.out.println("You can login to your account.");
                        }
                    break;
                case '2':
                    Terminal terminal = TerminalBuilder.terminal();
                    LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

                    String nationalID = reader.readLine("Enter your national ID: ");
                    String password = reader.readLine("Enter your password: ", '*');
                    User loggedInUser = AuthenticationService.login(exsistingAllUsers, nationalID, password);
                    if (loggedInUser != null) {

                        switch (loggedInUser.getUserType()) {
                            case ADMIN:
                                Admin adminUser = (Admin) loggedInUser;
                                AdminMenu adminMenu = new AdminMenu();
                                adminMenu.handleAdminMenu(adminUser,exsistingAllUsers, dataAccess);    
                            break;
                            case MANAGER:
                                Manager managerUser = (Manager) loggedInUser;
                                ManagerMenu managerMenu = new ManagerMenu();
                                ArrayList<EndUser> endUsers =  dataAccess.filterUsersByType(exsistingAllUsers, UserRole.ENDUSER, EndUser.class);
                                managerMenu.handleManagerMenu(managerUser,endUsers);
                            break;
                            case ENDUSER:
                                EndUser endUser = (EndUser) loggedInUser;
                                
                            break;
                            default:
                                System.out.println("Invalid user type. Please contact support.");
                        }
                    }
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
        System.out.println("1. Create new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Please select an option: ");
    }
}
