package services;

import models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private Map<Integer, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    // Add a new user with a hashed password
    public void addUser(User user, String plainPassword) {
        String hashedPassword = hashPassword(plainPassword); // Hash the password
        user.setPasswordHash(hashedPassword);
        users.put(user.getId(), user);
        System.out.println("User " + user.getFirstName() + " added successfully.");
    }

    // Hash the password using BCrypt
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify the user's password during login
    public boolean verifyPassword(User user, String plainPassword) {
        return BCrypt.checkpw(plainPassword, user.getPasswordHash());
    }

    // Find a user by ID
    public User findUserById(int id) {
        return users.get(id);
    }

    // Update password for a user
    public void updatePassword(int id, String newPlainPassword) {
        User user = users.get(id);
        if (user != null) {
            String hashedPassword = hashPassword(newPlainPassword);
            user.setPasswordHash(hashedPassword);
            System.out.println("Password updated successfully for user: " + user.getFirstName());
        } else {
            System.out.println("User not found.");
        }
    }
    public List<User> getAllUsers() {
        List<User> onlyUsers = new ArrayList<>(users.size());
        onlyUsers.addAll(users.values());
        return onlyUsers; // Return a copy to prevent external modifications
    }
}
