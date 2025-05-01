package com.banking.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    /**
     * Encrypts a file using AES encryption
     * @param inputFile path to input file
     * @param outputFile path to output encrypted file
     * @param key encryption key
     * @throws Exception if encryption fails
     */
    public static void encryptFile(String inputFile, String outputFile, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
            }
            
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        }
    }
    
    /**
     * Generates a new encryption key
     * @return SecretKey generated key
     * @throws Exception if key generation fails
     */
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // 256-bit key size
        return keyGen.generateKey();
    }
    
    /**
     * Decrypts a file using AES encryption
     * @param inputFile path to encrypted input file
     * @param outputFile path to output decrypted file
     * @param key decryption key (must be same as encryption key)
     * @throws Exception if decryption fails
     */
    public static void decryptFile(String inputFile, String outputFile, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
            }
            
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        }
    }
}