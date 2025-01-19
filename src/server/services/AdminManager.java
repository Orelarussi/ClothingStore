package server.services;
import server.models.Branch;
import server.services.BranchManager;

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

    //singleton
    private static AdminManager instance;
    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }

    private AdminManager(){}


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

    public List<Employee> getEmployeesByBranch(int branchID) {
        List<Employee> employees = getAllEmployees();
        List<Employee> filtered = employees.stream()
                .filter(emp -> emp.getBranchID() == branchID)
                .toList();
        return filtered;
    }

    public void addEmployee(Employee employee) {

        employees.put(employee.getId(), employee);
        Branch branch =BranchManager.getInstance().getBranchById(employee.getBranchID());
        branch.increaseEmployeeNumberBy1();

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
                if (value instanceof Integer) {
                    employee.setBranchID((int) value);
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
