package server.services;

import models.Admin;
import models.Employee;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManager {
    private Map<Integer, Employee> employees = new HashMap<>();
    private static final Admin admin = new Admin(1, "Eran", "", "000", "1234");

    // Verify the employee's password during login
    public User login(int id, String pass) {
        if (admin.getId() == id && admin.getPassword().equals(pass))
            return admin;

        Employee emp = employees.get(id);
        if (emp != null && emp.getPassword().equals(pass))
            return emp;

        return null;
    }

    // Update password for a employee
    public void updatePassword(int id, String password) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.setPassword(password);
            System.out.println("Password updated successfully for employee: " + employee.getFirstName());
        } else {
            System.out.println("Employee not found.");
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> onlyEmployees = new ArrayList<>(employees.size());
        onlyEmployees.addAll(employees.values());
        return onlyEmployees; // Return a copy to prevent external modifications
    }

    public void updateEmployee(Employee emp) {
        employees.put(emp.getId(), emp);
        System.out.println("Employee updated successfully for employee: " + emp.getFirstName());
    }


    public List<Employee> getEmployeesByBranch(String branchID) {
        List<Employee> employees = getAllEmployees();
        List<Employee> filtered = employees.stream().filter(emp -> emp.getBranchID().equals(branchID)).toList();
        return filtered;
    }

    // Add a new employee
    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
        System.out.println("Employee " + employee.getFullName() + " added successfully.");
    }

    // Update employee password
    public void updateEmployeePassword(int id, String pass) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.setPassword(pass);
            System.out.println("Password updated successfully for employee: " + employee.getFirstName());
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
            return passwordHash.equals(employee.getPassword());
        }
        return false;
    }
}
