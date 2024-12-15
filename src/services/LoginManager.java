package services;

import models.User;
import java.util.HashMap;

public class LoginManager {
    private HashMap<Integer, User> users;

    public LoginManager() {
        this.users = new HashMap<>();
    }

    // Add a new user to the system
    public void addUser(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            System.out.println("User with ID " + user.getId() + " already exists.");
        }
    }

    // Authenticate user by ID and password hash
    public boolean authenticate(int id, String passwordHash) {
        User user = users.get(id);
        if (user != null && user.getPasswordHash().equals(passwordHash)) {
            System.out.println("Login successful for user: " + user.getFirstName());
            return true;
        } else {
            System.out.println("Authentication failed. Please check your ID or password.");
            return false;
        }
    }
}
