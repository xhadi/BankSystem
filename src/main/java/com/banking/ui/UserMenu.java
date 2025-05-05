package com.banking.ui;

import java.util.Scanner;
import java.util.List;
import com.banking.auth.EndUser;
import com.banking.core.Account;
import com.banking.utils.ValidationUtils;

public class UserMenu {
    private EndUser currentUser;

    public UserMenu(EndUser currentUser) {
        this.currentUser = currentUser;
    }

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
        Scanner scanner = new Scanner(System.in);
        char input;
        do {
            displayUserMenu();
            input = scanner.nextLine().charAt(0);
            switch (input) {
                case '1':
                    List<Account> accounts = currentUser.getAccounts();
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts found.");
                    } else {
                        for (Account acc : accounts) {
                            acc.displayInformations();
                        }
                    }
                    break;
                case '2':
                    System.out.println("Resetting password...");
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    if (ValidationUtils.isValidPassword(newPassword)) {
                        currentUser.setPassword(newPassword);
                        System.out.println("Password reset successfully.");
                    } else {
                        System.out.println("Password must be at least 8 characters with uppercase, lowercase, number, and special character.");
                    }
                    break;
                case '3':
                    System.out.print("Enter deposit amount: ");
                    String depositInput = scanner.nextLine();
                    try {
                        double depositAmount = Double.parseDouble(depositInput);
                        currentUser.deposit(depositAmount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount format.");
                    }
                    break;
                case '4':
                    System.out.print("Enter withdrawal amount: ");
                    String withdrawInput = scanner.nextLine();
                    try {
                        double withdrawAmount = Double.parseDouble(withdrawInput);
                        currentUser.withdrawal(withdrawAmount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount format.");
                    }
                    break;
                case '5':
                    System.out.print("Enter target account number: ");
                    String targetAccount = scanner.nextLine();
                    System.out.print("Enter transfer amount: ");
                    String transferInput = scanner.nextLine();
                    try {
                        double transferAmount = Double.parseDouble(transferInput);
                        boolean success = currentUser.transfer(targetAccount, transferAmount);
                        System.out.println(success ? "Transfer successful." : "Transfer failed.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount format.");
                    }
                    break;
                case '6':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input != '6');
        scanner.close();
    }
}