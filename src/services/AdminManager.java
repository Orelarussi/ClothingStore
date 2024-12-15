package services;

import models.Employee;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.PasswordValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminManager {
    private Map<Integer, Employee> employees;

    public AdminManager() {
        this.employees = new HashMap<>();
    }

    // Add a new employee
    public void addEmployee(Employee employee, String plainPassword) {
        // Validate the plain password (check against any predefined password rules)
        if (PasswordValidator.validate(plainPassword, "")) {  // Assuming you are validating the password against some rule, we pass an empty string as the second argument
            String hashedPassword = PasswordValidator.convertToHash(plainPassword);
            employee.setPasswordHash(hashedPassword);
            employees.put(employee.getId(), employee);
            System.out.println("Employee " + employee.getFirstName() + " added successfully.");
        } else {
            System.out.println("Password does not meet the required criteria.");
        }
    }

    // Update employee password
    public void updateEmployeePassword(int id, String newPlainPassword) {
        Employee employee = employees.get(id);
        if (employee != null) {
            if (PasswordValidator.validate(newPlainPassword,"")) {
                String hashedPassword = PasswordValidator.convertToHash(newPlainPassword);
                employee.setPasswordHash(hashedPassword);
                System.out.println("Password updated successfully for employee: " + employee.getFirstName());
            } else {
                System.out.println("New password does not meet the required criteria.");
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

    // Delete an employee
    public void deleteEmployee(int id) {
        Employee employee = employees.remove(id);
        if (employee != null) {
            System.out.println("Employee " + employee.getFirstName() + " removed successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

//    // Validate password (minimum length, contains digits, special characters, etc.)
//    private boolean isPasswordValid(String password) {
//        if (password.length() < 8) {
//            System.out.println("Password must be at least 8 characters long.");
//            return false;
//        }
//        if (!password.matches(".*[A-Z].*")) {
//            System.out.println("Password must contain at least one uppercase letter.");
//            return false;
//        }
//        if (!password.matches(".*\\d.*")) {
//            System.out.println("Password must contain at least one digit.");
//            return false;
//        }
//        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
//            System.out.println("Password must contain at least one special character.");
//            return false;
//        }
//        return true;
//    }

    // List all employees
    public void listEmployees() {
        for (Employee employee : employees.values()) {
            System.out.println(employee);
        }
    }

    public Employee findEmployeeById(int id) {
        return employees.get(id);
    }
    // Verify employee password
    public boolean verifyEmployeePassword(int id, String passwordHash) {
        Employee employee = findEmployeeById(id);
        if (employee != null) {
            return passwordHash.equals(employee.getPasswordHash());
        }
        return false;
    }
}
