package com.banking.ui;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import com.banking.auth.*;
import com.banking.data.DataAccess;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MainMenu {

    public MainMenu(){

    }

    public void handleMainMenu(DataAccess dataAccess, ArrayList<User> existingUsers, LineReader reader, Terminal terminal) throws ParseException, IOException {
        try {
            char input;
            System.out.println("Welcome to the Banking System");
            do {
                displayMenu();
                // Use JLine's reader for all input (not Scanner)
                String choice = reader.readLine("Please select an option: ");
                input = choice.isEmpty() ? ' ' : choice.charAt(0);

                switch (input) {
                    case '1' -> {
                        EndUser newUser = new EndUser();
                        newUser = newUser.createUser(reader, existingUsers); // Pass reader
                        if (newUser != null) {
                            existingUsers.add(newUser);
                            dataAccess.saveAllUsers(existingUsers);
                            System.out.println("User created successfully!");
                        } else {
                            System.out.println("Failed to create user.");
                        }
                    }
                    case '2' -> {
                        String nationalID = reader.readLine("Enter your national ID: ");
                        // Mask password with '*' every time
                        String password = reader.readLine("Enter your password: ", '*');
                        User loggedInUser = AuthenticationService.login(existingUsers, nationalID, password, dataAccess);
                        if (loggedInUser != null) {
                            switch (loggedInUser.getUserType()) {
                                case ADMIN -> {
                                    Admin adminUser = (Admin) loggedInUser;
                                    AdminMenu adminMenu = new AdminMenu();
                                    adminMenu.handleAdminMenu(adminUser, existingUsers, dataAccess, reader);
                                }
                                case MANAGER -> {
                                    Manager managerUser = (Manager) loggedInUser;
                                    ManagerMenu managerMenu = new ManagerMenu();
                                    ArrayList<EndUser> endUsers = dataAccess.filterUsersByType(existingUsers, UserRole.ENDUSER, EndUser.class);
                                    managerMenu.handleManagerMenu(managerUser, endUsers, dataAccess, reader, existingUsers);
                                }
                                case ENDUSER -> {
                                    EndUser endUser = (EndUser) loggedInUser;
                                    UserMenu userMenu = new UserMenu(endUser);
                                    userMenu.handleUserMenu(reader); // Pass reader
                                }
                                default -> System.out.println("Invalid user type.");
                            }
                        } else {
                            System.out.println("Incorrect National ID or Password");
                        }
                    }
                    case '3' -> {
                        System.out.println("Exiting the system. Goodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option.");
                }
            } while (input != '3');
        } 
            finally {
                // Only try to close the terminal if it's interactive and not already closed
                try {
                    if (terminal != null && terminal.getType() != null && !terminal.getType().equals("dumb")) {
                        terminal.close();
                    }
                } catch (IOException e) {
                    // Log the error but don't let it crash the program
                    System.out.println("Terminal close error (non-critical): " + e.getMessage());
                }
            }
        
    }

    public void displayMenu() {
        System.out.println("1. Create new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }
}