package com.banking.ui;

import org.jline.reader.LineReader;
import java.util.ArrayList;
import com.banking.auth.AuthenticationService;
import com.banking.auth.EndUser;
import com.banking.core.Account;
import com.banking.utils.ValidationUtils;

public class UserMenu {
    private EndUser currentUser;

    public UserMenu(EndUser currentUser) {
        this.currentUser = currentUser;
    }

    public void displayUserMenu() {
        System.out.println("\nWelcome to the User Menu");
        System.out.println("1. View Account Details");
        System.out.println("2. Reset Password");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Money");
        System.out.println("6. Logout");
    }

    public void handleUserMenu(LineReader reader) {
        String input;
        do {
            displayUserMenu();
            // Use LineReader for all input
            input = reader.readLine("Please select an option: ").trim();
            if (input.isEmpty()) continue;

            switch (input.charAt(0)) {
                case '1':
                    ArrayList<Account> accounts = currentUser.getAccounts();
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts found.");
                    } else {
                        accounts.forEach(Account::displayInformations);
                    }
                    break;
                case '2':
                    System.out.println("Resetting password...");
                    // Mask password input with '*' using LineReader
                    String newPassword = reader.readLine("Enter new password: ", '*');
                    if (ValidationUtils.isValidPassword(newPassword)) {
                        currentUser.setPassword(AuthenticationService.hashPassword(newPassword));
                        System.out.println("Password reset successfully.");
                    } else {
                        System.out.println("Invalid password. Must be 8+ characters with uppercase, lowercase, number, and special character.");
                    }
                    break;
                case '3':
                    String deposit = reader.readLine("Enter deposit amount: $").trim();
                    double depositAmount = Double.parseDouble(deposit);
                    currentUser.deposit(reader, depositAmount);
                    break;
                case '4':
                    handleAmountInput(reader, "withdraw", currentUser::withdrawal);
                    break;
                case '5':
                    System.out.print("Enter target account number: ");
                    String targetAccount = reader.readLine().trim();
                    handleAmountInput(reader, "transfer", amount -> 
                        currentUser.transfer(targetAccount, amount));
                    break;
                case '6':
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (input.charAt(0) != '6');
    }

    private void handleAmountInput(LineReader reader, String actionName, AmountConsumer action) {
        String input = reader.readLine("Enter " + actionName + " amount: $").trim();
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            boolean success = action.apply(amount);
            System.out.println(success ? actionName + " successful." : actionName + " failed.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
        }
    }

    @FunctionalInterface
    private interface AmountConsumer {
        boolean apply(double amount);
    }
}