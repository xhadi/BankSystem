package com.banking.ui;

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
    private Terminal terminal; // Initialize once
    private LineReader reader; // Reuse for input

    public MainMenu() throws IOException {
        try {
            // Initialize terminal once
            terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();
        } catch (IOException e) {
            // Fallback to dumb terminal if necessary
            terminal = TerminalBuilder.builder().dumb(true).build();
        }
        reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build();
    }

    public void handleMainMenu(DataAccess dataAccess, ArrayList<User> existingUsers) throws ParseException, IOException {
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
                                    adminMenu.handleAdminMenu(adminUser, existingUsers, dataAccess);
                                }
                                case MANAGER -> {
                                    Manager managerUser = (Manager) loggedInUser;
                                    ManagerMenu managerMenu = new ManagerMenu();
                                    ArrayList<EndUser> endUsers = dataAccess.filterUsersByType(existingUsers, UserRole.ENDUSER, EndUser.class);
                                    managerMenu.handleManagerMenu(managerUser, endUsers, dataAccess);
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
        } finally {
            if (terminal != null) {
                terminal.close(); // Cleanup terminal
            }
        }
    }

    public void displayMenu() {
        System.out.println("1. Create new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }
}