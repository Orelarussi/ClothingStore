package server.services;

import models.Employee;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private Map<Integer, Employee> employees;

    public LoginManager() {
        this.employees = new HashMap<>();
    }

    // Add a new employee to the system
    public void addUser(Employee employee) {
        if (!employees.containsKey(employee.getId())) {
            employees.put(employee.getId(), employee);
        } else {
            System.out.println("User with ID " + employee.getId() + " already exists.");
        }
    }

    // Authenticate user by ID and password hash
    public boolean login(int id, String password) {
        Employee employee = employees.get(id);
        if (employee != null && employee.getPassword().equals(password)) {
            System.out.println("Login successful for employee: " + employee.getFirstName());
            return true;
        } else {
            System.out.println("Authentication failed. Please check your ID or password.");
            return false;
        }
    }
}
