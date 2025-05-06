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
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main {
    private static final String ENCRYPTED_ACCOUNTS_FILE = "accounts.csv.enc";
    private static SecretKey encryptionKey;

    public static void main(String[] args) {
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
            mainMenu.handleMainMenu(dataAccess, existingUsers);

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