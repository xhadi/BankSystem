package com.banking;

import com.banking.auth.Admin;
import com.banking.auth.EndUser;
import com.banking.auth.Manager;
import com.banking.auth.User;
import com.banking.config.Config;
import com.banking.data.DataAccess;
import com.banking.ui.MainMenu;
import com.banking.utils.EncryptionUtils;
import javax.crypto.SecretKey;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main {
    private static final String ENCRYPTED_ACCOUNTS_FILE = "data/accounts.csv.enc";
    private static SecretKey encryptionKey;

    public static void main(String[] args) {
        Terminal terminal = null;
        LineReader reader = null;
        try {
            // Initialize encryption
            initializeEncryption();

            // Decrypt accounts file if exists
            decryptAccountsFile();

            // Setup shutdown hook for encryption
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    encryptAccountsFile();
                    Files.deleteIfExists(new File(Config.ACCOUNTS_FILE).toPath());
                } catch (Exception e) {
                    System.err.println("Error during shutdown cleanup: " + e.getMessage());
                }
            }));

            try {
                terminal = TerminalBuilder.builder()
                        .system(true)
                        .jna(true)
                        .build();
            } catch (IOException e) {
                // If interactive terminal fails, fall back to a dumb terminal
                System.out.println("Warning: Interactive terminal not available. Falling back to basic input mode.");
                terminal = TerminalBuilder.builder()
                        .dumb(true)
                        .build();
            }

            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
            
            // Main application logic
            DataAccess dataAccess = new DataAccess();
            ArrayList<User> existingUsers = new ArrayList<>();
            ArrayList<User> loadedUsers = new ArrayList<>();
            dataAccess.loadAllUsers(loadedUsers);
            for (User user : loadedUsers) {
                switch (user.getUserType()) {
                    case ADMIN -> existingUsers.add(new Admin(user));
                    case MANAGER -> existingUsers.add(new Manager(user));
                    case ENDUSER -> existingUsers.add(new EndUser(user, dataAccess));
                }
            }

            MainMenu mainMenu = new MainMenu();
            // Pass reader to main menu handling
            mainMenu.handleMainMenu(dataAccess, existingUsers, reader, terminal);

        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeEncryption() throws Exception {
        File keyFile = new File("encryption.key");
        if (keyFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile))) {
                encryptionKey = (SecretKey) ois.readObject();
            }
        } else {
            encryptionKey = EncryptionUtils.generateKey();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFile))) {
                oos.writeObject(encryptionKey);
            }
        }
    }

    private static void decryptAccountsFile() throws Exception {
        File encryptedFile = new File(ENCRYPTED_ACCOUNTS_FILE);
        if (encryptedFile.exists()) {
            EncryptionUtils.decryptFile(
                    ENCRYPTED_ACCOUNTS_FILE,
                    Config.ACCOUNTS_FILE,
                    encryptionKey);
        }
    }

    private static void encryptAccountsFile() throws Exception {
        EncryptionUtils.encryptFile(
                Config.ACCOUNTS_FILE,
                ENCRYPTED_ACCOUNTS_FILE,
                encryptionKey);
    }
}