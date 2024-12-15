package services;

import models.User;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private Set<User> loggedInUsers;

    public SessionManager() {
        this.loggedInUsers = new HashSet<>();
    }

    // Check if user is already logged in
    public boolean isUserLoggedIn(User user) {
        return loggedInUsers.contains(user);
    }

    // Log in a user
    public boolean logInUser(User user) {
        if (isUserLoggedIn(user)) {
            System.out.println("User " + user.getFirstName() + " is already logged in.");
            return false;
        } else {
            loggedInUsers.add(user);
            System.out.println("User " + user.getFirstName() + " logged in successfully.");
            return true;
        }
    }

    // Log out a user
    public void logOutUser(User user) {
        if (loggedInUsers.remove(user)) {
            System.out.println("User " + user.getFirstName() + " logged out successfully.");
        } else {
            System.out.println("User " + user.getFirstName() + " is not logged in.");
        }
    }
}
