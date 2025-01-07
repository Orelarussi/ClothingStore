package server.services;

import server.models.Admin;
import server.models.Employee;
import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManager {
    private Map<Integer, Employee> employees = new HashMap<>();
    private static final Admin admin = new Admin(1, "Eran", "", "000", "1234");
    private static AdminManager instance;

    private AdminManager(){}

    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }
    public LoginResult login(int id, String pass) {
        // Check if the login is for the admin
        if (admin.getId() == id && admin.getPassword().equals(pass)) {
            return LoginResult.ADMIN;
        }

        // Check if the login is for an employee
        Employee emp = employees.get(id);
        if (emp != null && emp.getPassword().equals(pass)) {
            return LoginResult.EMPLOYEE;
        }

        // Return FAILURE if login credentials are incorrect
        return LoginResult.FAILURE;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> onlyEmployees = new ArrayList<>(employees.size());
        onlyEmployees.addAll(employees.values());
        return onlyEmployees; // Return a copy to prevent external modifications
    }

    public void listEmployees() {
        for (Employee employee : employees.values()) {
            System.out.println(employee);
        }
    }

    public List<Employee> getEmployeesByBranch(String branchID) {
        List<Employee> employees = getAllEmployees();
        List<Employee> filtered = employees.stream().filter(emp -> emp.getBranchID().equals(branchID)).toList();
        return filtered;
    }

    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
        System.out.println("Employee " + employee.getFullName() + " added successfully.");
    }

    public void deleteEmployee(int id) {
        Employee employee = employees.remove(id);
        if (employee != null) {
            System.out.println("Employee " + employee.getFirstName() + " removed successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    public <T> void updateEmployee(int employeeID, String attribute, T value) {
        Employee employee = employees.get(employeeID);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeID);
        }

        // Update the specified attribute
        switch (attribute.toLowerCase()) {
            case "firstname":
                if (value instanceof String) {
                    employee.setFirstName((String) value);
                } else {
                    throw new IllegalArgumentException("Invalid value type for 'firstname'. Expected: String.");
                }
                break;
            case "lastname":
                if (value instanceof String) {
                    employee.setLastName((String) value);
                } else {
                    throw new IllegalArgumentException("Invalid value type for 'lastname'. Expected: String.");
                }
                break;
            case "branchid":
                if (value instanceof String) {
                    employee.setBranchID((String) value);
                } else {
                    throw new IllegalArgumentException("Invalid value type for 'branchid'. Expected: String.");
                }
                break;
            case "password":
                if (value instanceof String) {
                    employee.setPassword((String) value);
                } else {
                    throw new IllegalArgumentException("Invalid value type for 'password'. Expected: String.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }

        // Save the updated employee back to the map
        employees.put(employeeID, employee);
    }

    public Employee findEmployeeById(int id) {
        return employees.get(id);
    }

    public boolean verifyEmployeePassword(int id, String passwordHash) {
        Employee employee = findEmployeeById(id);
        if (employee != null) {
            return passwordHash.equals(employee.getPassword());
        }
        return false;
    }
}
