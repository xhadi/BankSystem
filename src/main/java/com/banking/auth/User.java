package com.banking.auth;

import com.banking.data.DataAccess;
import com.banking.utils.ValidationUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class User {
    // Attributes shared by all users
    private String nationalID;
    private String firstName;
    private String fatherName;
    private String familyName;
    private String fullName = firstName + " " + fatherName + " " + familyName;
    private Date dateOfBirth;
    private String phone;
    private String username;
    private String password;
    private UserRole userType;
    private Boolean status;
    
    public User(){

    }
    
    public User(String nationalID, String fullName, Date dateOfBirth, String phone,
                String username, String password, UserRole userType, Boolean status) {
        this.nationalID = nationalID;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.status = status;
    }

    public User(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
                String username, String password, UserRole userType, boolean status) {
        this.nationalID = nationalID;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.status = status;
    }

    public User(String nationalID, String firstName, String fatherName, String familyName, Date dateOfBirth, String phone,
                String username, String password) {
        this.nationalID = nationalID;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.password = password;
        userType = UserRole.ENDUSER;
        this.status = true;
    }

    // Getters and Setters
    public String getNationalID() {
        return nationalID;
    }
    public void setNationalID(String nationalID) {
        this.nationalID = nationalID; 
    }

    public String getFullName() {
        return fullName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone; 
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = AuthenticationService.hashPassword(password);
    }

    public UserRole getUserType() {
        return userType; 
    }
    public void setUserType(UserRole userType) {
        this.userType = userType; 
    }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public boolean isActive() {
        return status;
    }

    public boolean updatePhone(String newPhone) {
        if (newPhone == null || !ValidationUtils.isValidPhone(newPhone)) {
            System.out.println("Invalid phone number.");
            return false;
        }
        this.phone = newPhone;
        System.out.println("Phone number updated successfully!");
        return true;
    }

    public boolean updateUsername(ArrayList<User> existingUsers ,String newUsername) {
        if (newUsername == null) {
            System.out.println("Invalid username.");
            return false;
        }
        else
        {
            if(!ValidationUtils.isValidUsername(newUsername, existingUsers)){
                System.out.println("Username already exists, please try again.");
                return false;
            }
        }
        this.username = newUsername;
        System.out.println("Username updated successfully!");
        return true;
    }

    public void viewPersonalInfo() {
        fullName = String.join(" ", firstName, fatherName, familyName);
        System.out.println("------------- Account Information -------------");
        System.out.printf("%-15s %s%n", "Full Name:", fullName);
        System.out.printf("%-15s %s%n", "Username:", username);
        System.out.printf("%-15s %s%n", "User Type:", userType);
        System.out.printf("%-15s %s%n", "Status:", (status ? "Active" : "Inactive"));
        System.out.printf("%-15s %s%n", "National ID:", nationalID);
        System.out.printf("%-15s %s%n", "Phone:", phone);
        System.out.println("-----------------------------------------------");
    }

    public EndUser createUser(LineReader reader, ArrayList<User> existingUsers) {
        try {
            System.out.println("\n=== Create New User ===");
    
            // National ID
            String nationalID = readValidatedInput(reader, "Enter national ID: ", 
                ValidationUtils::isValidNationalID);
    
            // Names
            String firstName = readValidatedInput(reader, "Enter first name: ", 
                ValidationUtils::isValidName);
            String fatherName = readValidatedInput(reader, "Enter father name: ", 
                ValidationUtils::isValidName);
            String familyName = readValidatedInput(reader, "Enter family name: ", 
                ValidationUtils::isValidName);
    
            // Date of Birth
            Date dateOfBirth = readValidDate(reader);
    
            // Phone Number
            String phone = readValidatedInput(reader, "Enter phone number: ", 
                ValidationUtils::isValidPhone);
    
            // Username
            String username = readValidatedInput(reader, "Enter username: ", 
                input -> ValidationUtils.isValidUsername(input, existingUsers));
    
            // Password
            String password = readPassword(reader);
    
            EndUser newUser = new EndUser(
                nationalID, firstName, fatherName, familyName, dateOfBirth,
                phone, username, AuthenticationService.hashPassword(password)
            );
            newUser.createAccount();
            return newUser;
    
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            return null;
        }
    }
    
    // Helper Methods
    private String readValidatedInput(LineReader reader, String prompt, 
                                      java.util.function.Predicate<String> validator) {
        String input;
        do {
            input = reader.readLine(prompt).trim();
        } while (!validator.test(input));
        return input;
    }
    
    private Date readValidDate(LineReader reader) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        
        Date date;
        do {
            String dateStr = readValidatedInput(reader, "Enter date of birth (dd-MM-yyyy): ", 
                s -> ValidationUtils.isValidDateOfBirth(s));
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use dd-MM-yyyy");
                date = null;
            }
        } while (date == null);
        return date;
    }
    
    private String readPassword(LineReader reader) {
        System.out.println("Password requirements:");
        System.out.println("- At least 8 characters");
        System.out.println("- Contains uppercase and lowercase letters");
        System.out.println("- Includes number and special character");
        
        return readValidatedInput(reader, "Enter new password: ", 
            ValidationUtils::isValidPassword, '*');
    }
    
    private String readValidatedInput(LineReader reader, String prompt, 
                                      java.util.function.Predicate<String> validator, 
                                      Character mask) {
        String input;
        do {
            input = reader.readLine(prompt, mask).trim();
        } while (!validator.test(input));
        return input;
    }

    public void editUserInformation(DataAccess dataAccess,ArrayList<User> existingUsers) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        
        
        boolean editing = true;
        boolean changed = false;
        
        while (editing) {
            System.out.println("\n========== Information Editor ==========");
            System.out.println("1. Update Phone Number");
            System.out.println("2. Change Username");
            System.out.println("3. Reset Password");
            System.out.println("4. Modify Name");
            System.out.println("5. Change Account Status");
            System.out.println("6. Change User Type");
            System.out.println("7. View Current Information");
            System.out.println("0. Exit Editor");
            System.out.print("Enter your choice: ");
    
            char choice;


            choice = scanner.nextLine().charAt(0);
    
            switch (choice) {
                case '1' -> {
                    System.out.print("Enter new phone number: ");
                    changed = updatePhone(scanner.nextLine());
                    if(changed){
                        dataAccess.saveAllUsers(existingUsers);
                    }
                }
                case '2' -> {
                    System.out.print("Enter new username: ");
                    changed = updateUsername(existingUsers, scanner.nextLine());
                    if(changed){
                        dataAccess.saveAllUsers(existingUsers);
                    }
                }
                case '3' -> {
                    System.out.println("Password requirements:");
                    System.out.println("- At least 8 characters");
                    System.out.println("- Contains uppercase and lowercase letters");
                    System.out.println("- Includes number and special character");
                    String newPassword = reader.readLine("Enter new password: ",'*');
                    if (ValidationUtils.isValidPassword(newPassword)) {
                        this.password = AuthenticationService.hashPassword(newPassword);
                        dataAccess.saveAllUsers(existingUsers);
                        System.out.println("Password successfully reset!");
                    } else {
                        System.out.println("Invalid password.");
                    }
                }
                case '4' -> {
                    System.out.println("\n--- Name Modification ---");
                    System.out.println("1. First Name");
                    System.out.println("2. Father's Name");
                    System.out.println("3. Family Name");
                    System.out.print("Select component to modify: ");
                    char nameChoice = scanner.nextLine().charAt(0);
                    
                    System.out.print("Enter new name: ");
                    String newValue = scanner.nextLine();
                    if (ValidationUtils.isValidName(newValue)) {
                        switch (nameChoice) {
                            case '1' -> setFirstName(newValue);
                            case '2' -> setFatherName(newValue);
                            case '3' -> setFamilyName(newValue);
                            default -> System.out.println("Invalid name selection");
                        }
                        updateFullName();
                        System.out.println("Name updated successfully");
                        dataAccess.saveAllUsers(existingUsers);

                    } else {
                        System.out.println("Invalid name format");
                    }
                }
                case '5' -> {
                    setStatus(!status);
                    System.out.println("Account status changed to: " + (status ? "Active" : "Inactive"));
                }
                case '6' -> {
                    System.out.println("Available user types:");
                    System.out.println("1. ENDUSER " + "2. Managr");
                    System.out.print("Enter new user type: ");
                    try {
                        String newType = scanner.nextLine().toUpperCase();
                        if (newType.equals("ADMIN")) {
                            System.out.println("Cannot set user type to ADMIN");
                            continue;
                        }
                        setUserType(UserRole.valueOf(newType));
                        System.out.println("User type updated");
                        dataAccess.saveAllUsers(existingUsers);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid user type");
                    }
                }
                case '7' -> viewPersonalInfo();
                case '0' -> {
                    editing = false;
                    scanner.close();
                    
                    System.out.println("Exiting editor...");
                }
                default -> System.out.println("Invalid selection");
            }
        }
    }
    
    // Add this helper method to update full name when components change
    private void updateFullName() {
        this.fullName = String.join(" ", firstName, fatherName, familyName);
    }

    public void editPersonalInformation(DataAccess dataAccess,ArrayList<User> existingUsers) throws IOException {
        boolean editing = true;
        boolean changed = false;

        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        
        while (editing) {
            System.out.println("\n========== Personal Information Editor ==========");
            System.out.println("1. Update Phone Number");
            System.out.println("2. Change Username");
            System.out.println("3. Change Password");
            System.out.println("4. Modify Name");
            System.out.println("5. View Current Information");
            System.out.println("0. Exit Editor");
            System.out.print("Enter your choice: ");
            
            Scanner scanner = new Scanner(System.in);
            char choice = scanner.nextLine().charAt(0);
    
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new phone number: ");
                    changed = updatePhone(scanner.nextLine());
                    if(changed){
                        dataAccess.saveAllUsers(existingUsers);
                    }
                }
                case 2 -> {
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    changed = updateUsername(existingUsers, newUsername);
                    if(changed){
                        dataAccess.saveAllUsers(existingUsers);
                    }
                }
                case 3 -> {
                    System.out.print("Enter current password: ");
                    String currentPassword = scanner.nextLine();
                    System.out.println("New password requirements:");
                    System.out.println("- At least 8 characters");
                    System.out.println("- Contains uppercase and lowercase letters");
                    System.out.println("- Includes number and special character");
                    String newPassword = reader.readLine("Enter new password: ", '*');
                    
                    if (AuthenticationService.verifyPassword(currentPassword, this.password)) {
                        if (ValidationUtils.isValidPassword(newPassword)) {
                            this.password = AuthenticationService.hashPassword(newPassword);
                            System.out.println("Password changed successfully!");
                            dataAccess.saveAllUsers(existingUsers);
                        } else {
                            System.out.println("Invalid new password");
                        }
                    } else {
                        System.out.println("Incorrect current password");
                    }
                }
                case 4 -> {
                    System.out.println("\n--- Name Modification ---");
                    System.out.println("1. First Name");
                    System.out.println("2. Father's Name");
                    System.out.println("3. Family Name");
                    System.out.print("Select component to modify: ");
                    char nameChoice = scanner.nextLine().charAt(0);
                    
                    System.out.print("Enter new Name: ");
                    String newValue = scanner.nextLine();
                    if (ValidationUtils.isValidName(newValue)) {
                        switch (nameChoice) {
                            case '1' -> setFirstName(newValue);
                            case '2' -> setFatherName(newValue);
                            case '3' -> setFamilyName(newValue);
                            default -> System.out.println("Invalid name component selection");
                        }
                        updateFullName();
                        System.out.println("Name updated successfully");
                        dataAccess.saveAllUsers(existingUsers);
                    } else {
                        System.out.println("Invalid name format");
                    }
                }
                case 5 -> viewPersonalInfo();
                case 0 -> {
                    editing = false;
                    scanner.close();
                    System.out.println("Exiting editor...");
                }
                default -> System.out.println("Invalid selection");
            }
        }
    }
}