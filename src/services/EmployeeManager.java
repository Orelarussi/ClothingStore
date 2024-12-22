package services;

import models.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeManager {
    private Map<Integer, Employee> employees;

    public EmployeeManager() {
        this.employees = new HashMap<>();
    }

    // Add a new employee with a hashed password
    public void addEmployee(Employee employee, String password) {
        employee.setPassword(password);
        employees.put(employee.getId(), employee);
        System.out.println("Employee " + employee.getFirstName() + " added successfully.");
    }


    // Verify the employee's password during login
    public boolean verifyPassword(Employee employee, String plainPassword) {
        return plainPassword.equals(employee.getPassword());
//        return BCrypt.checkpw(plainPassword, employee.getPassword());
    }

    // Find a employee by ID
    public Employee findEmployeeById(int id) {
        return employees.get(id);
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
}
