package utils;
import java.util.regex.Pattern;

public class PasswordValidator {
    // Password criteria
    private static final int MIN_LENGTH = 8; // Minimum length
    private static final Pattern UPPER_CASE = Pattern.compile(".*[A-Z].*"); // At least one uppercase letter
    private static final Pattern LOWER_CASE = Pattern.compile(".*[a-z].*"); // At least one lowercase letter
    private static final Pattern DIGIT = Pattern.compile(".*[0-9].*"); // At least one digit
    private static final Pattern SPECIAL_CHAR = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"); // At least one special character

    /**
     * Validates if a password meets the criteria
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            System.out.println("Error: Password must be at least " + MIN_LENGTH + " characters long.");
            return false;
        }
        if (!UPPER_CASE.matcher(password).matches()) {
            System.out.println("Error: Password must contain at least one uppercase letter.");
            return false;
        }
        if (!LOWER_CASE.matcher(password).matches()) {
            System.out.println("Error: Password must contain at least one lowercase letter.");
            return false;
        }
        if (!DIGIT.matcher(password).matches()) {
            System.out.println("Error: Password must contain at least one digit.");
            return false;
        }
        if (!SPECIAL_CHAR.matcher(password).matches()) {
            System.out.println("Error: Password must contain at least one special character.");
            return false;
        }

        // If all criteria are met
        return true;
    }
}
