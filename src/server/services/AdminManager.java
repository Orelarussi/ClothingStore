package server.services;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import server.exceptions.IllegalFieldValueException;
import server.models.Admin;
import server.models.Employee;
import server.utils.JsonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AdminManager implements MapChangeListener<Integer, Employee> {
    private ObservableMap<Integer, Employee> employees = FXCollections.observableHashMap();
    private static final Admin admin = new Admin(1, "Eran", "karaso", "000", "1234");
    public static int currentUserId = admin.getId();

    //singleton
    private static AdminManager instance;

    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }

    private AdminManager() {
        setEmployees(new ArrayList<>());
    }


    public LoginResult login(int id, String pass) {
         System.out.println("Attempting login for ID: " + id);
        LoginResult result = LoginResult.FAILURE;

        // Check if the login is for the admin
        if (admin.getId() == id && admin.getPassword().equals(pass)) {
            currentUserId = id;
            result = LoginResult.ADMIN;
            result.setMessage(admin.getFullName());
             System.out.println("Admin login successful for ID: " + id);
            return result;
        }

        // Check if the login is for an employee
        Employee emp = employees.get(id);
        if (emp != null && emp.getPassword().equals(pass)) {
            currentUserId = id;
            result = LoginResult.EMPLOYEE;
            result.setMessage(emp.getFullName());
             System.out.println("Employee login successful for ID: " + id);
            return result;
        }

        // Return FAILURE if login credentials are incorrect
        result.setMessage("Username or password is incorrect. Please try again.");
        return result;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> onlyEmployees = new ArrayList<>(employees.size());
        onlyEmployees.addAll(employees.values());
         System.out.println("Fetching all employees.");
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

    public String getEmployeeNameById(int employeeId) {
        Employee employee = findEmployeeById(employeeId);
        if (employee != null) {
            return employee.getFirstName() + " " + employee.getLastName();
        }
        return "Unknown Employee";
    }

    public void addEmployee(Employee employee) {
         System.out.println("Attempting to add employee with ID: " + employee.getId());
        if (employees.containsKey(employee.getId())) {

            String error = "Employee with ID " + employee.getId() + " already exists.";
            System.out.println(error);
            throw new IllegalArgumentException("Employee with id " + employee.getId() + " already exists");
        }
        if (admin.getId() == employee.getId()) {
            String error = "Admin with ID " + employee.getId() + " already exists.";
            System.out.println(error);
            throw new IllegalArgumentException("Admin with id " + employee.getId() + " already exists");
        }
        employees.put(employee.getId(), employee);
        BranchManager.getInstance().addEmployeeToBranch(employee.getBranchID());
        System.out.println("Employee " + employee.getFullName() + " added successfully.");
    }

    public void deleteEmployee(int id) {
         System.out.println("Attempting to delete employee with ID: " + id);
        Employee employee = employees.remove(id);
        if (employee != null) {
            System.out.println("Employee " + employee.getFirstName() + " removed successfully.");
            BranchManager.getInstance().removeEmployeeFromBranch(employee.getBranchID());
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void editEmployee(int employeeID, String attribute, String value)
            throws IllegalArgumentException, IllegalAccessException {
         System.out.println("Attempting to edit employee with ID: " + employeeID);

        Employee employee = employees.get(employeeID);
        if (employee == null) {
            String error = "Employee not found with ID: " + employeeID;
            System.out.println(error);
            throw new IllegalArgumentException("Employee not found with ID: " + employeeID);
        }

        List<Field> fields = Employee.getAllFields();
        Field field = fields.stream()
                .filter(f -> f.getName().equals(attribute))
                .findFirst().orElse(null);

        if (field == null) {
            String error = "Attribute " + attribute + " not found.";
            System.out.println(error);
            throw new IllegalArgumentException("Employee not found with attribute: " + attribute);
        }

        field.setAccessible(true);

        try {
            if (field.getType() == int.class) {
                int newNum = Integer.parseInt(value);
                if (field.getName().contains("branch")) {
                    BranchManager manager = BranchManager.getInstance();
                    manager.removeEmployeeFromBranch(employee.getBranchID());
                    manager.addEmployeeToBranch(newNum);
                }
                field.set(employee, newNum);

            } else if (field.getType() == long.class) {
                field.set(employee, Long.valueOf(value));

            } else if (field.getType() == Employee.Position.class) {
                field.set(employee, Employee.Position.valueOf(value));

            } else { //string default
                field.set(employee, value);
            }
             System.out.println("Updated attribute " + attribute + " for employee with ID: " + employeeID);
        } catch (IllegalArgumentException e) {
            String message = "The value " + value + " is not valid for field " + field.getName();
            System.out.println(message);
            throw new IllegalFieldValueException(message);
        }

        // Save the updated employee back to the map
        employees.remove(employeeID); //remove
        employees.put(employeeID, employee); //add to trigger onChange function
    }

    public Employee findEmployeeById(int id) {
         System.out.println("Searching for employee with ID: " + id);
        return employees.get(id);
    }

    public boolean verifyEmployeePassword(int id, String passwordHash) {
         System.out.println("Verifying password for employee with ID: " + id);
        Employee employee = findEmployeeById(id);
        if (employee != null) {
            return passwordHash.equals(employee.getPassword());
        }
        return false;
    }

    public void setEmployees(List<Employee> employees) {
         System.out.println("Setting employees.");
        ObservableMap<Integer, Employee> map = FXCollections.observableHashMap();
        for (Employee employee : employees) {
            map.put(employee.getId(), employee);
        }
        if (this.employees != null)
            this.employees.removeListener(this);
        this.employees = map;
        this.employees.addListener(this);
         System.out.println("Employees have been set successfully.");
    }

    @Override
    public void onChanged(Change<? extends Integer, ? extends Employee> change) {
        if (change.wasAdded()) {
             System.out.println("Employee added: " + change.getValueAdded());
        }
        else if (change.wasRemoved()) {
             System.out.println("Employee removed: " + change.getValueRemoved());
        }
        JsonUtils.saveEmployees();
         System.out.println("Employee changes saved.");
    }
}
