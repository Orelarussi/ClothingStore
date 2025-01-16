package server.services;

import server.models.Admin;
import server.models.Employee;
import server.models.User;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    // singleton
    private static LoginManager instance;
    public static LoginManager getInstance() {
        return instance != null ? instance : (instance = new LoginManager());
    }

    private final Map<Integer, User> users;

    private LoginManager() {
        this.users = new HashMap<>();
        users.put(1,new Admin(1, "Eran", "", "000", "1234"));//TODO remove after testing
    }


    // Add a new employee to the system
    public void addUser(Employee employee) {
        if (!users.containsKey(employee.getId())) {
            users.put(employee.getId(), employee);
        } else {
            System.out.println("User with ID " + employee.getId() + " already exists.");
        }
    }

    // Authenticate user by ID and password hash
    public User login(int id, String password) {
        User user = users.get(id);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful for user: " + user.getFirstName());
            return user;
        } else {
            System.out.println("Authentication failed. Please check your ID or password.");
            return null;
        }
    }
}
