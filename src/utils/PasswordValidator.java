package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordValidator {

    // Convert the password to a hash using SHA-256
    public static String convertToHash(String password) {
        return password;
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = digest.digest(password.getBytes());
//            StringBuilder hexString = new StringBuilder();
//
//            // Convert the byte array to a hexadecimal string
//            for (byte b : hashedBytes) {
//                hexString.append(String.format("%02x", b));
//            }
//
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error while hashing the password", e);
//        }
    }

    // Validate the password by comparing the provided password hash with the stored hash
    public static boolean validate(String password, String storedHash) {
        // Convert the provided password to hash and compare with stored hash
        String hashedPassword = convertToHash(password);
        return hashedPassword.equals(storedHash);
    }
}
