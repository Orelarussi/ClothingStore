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

import static server.logger.Logger.log;

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
        log("Fetching all employees.");
        return onlyEmployees; // Return a copy to prevent external modifications
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
            log("Get employee by ID: " + employeeId + " " + employee);
            return employee.getFirstName() + " " + employee.getLastName();
        }
        log("Unknown employee ID: " + employeeId);
        return "Unknown Employee";
    }

    public void addEmployee(Employee employee) {
        System.out.println("Attempting to add employee with ID: " + employee.getId());
        if (employees.containsKey(employee.getId())) {
            String error = "Employee with ID " + employee.getId() + " already exists.";
            System.out.println(error);
            log(error);
            throw new IllegalArgumentException("Employee with id " + employee.getId() + " already exists");
        }
        if (admin.getId() == employee.getId()) {
            String error = "Admin with ID " + employee.getId() + " already exists.";
            System.out.println(error);
            log(error);
            throw new IllegalArgumentException("Admin with id " + employee.getId() + " already exists");
        }
        employees.put(employee.getId(), employee);
        BranchManager.getInstance().addEmployeeToBranch(employee.getBranchID());
        String s = "Employee " + employee.getFullName() + " added successfully.";
        System.out.println(s);
        log(s);
    }

    public void deleteEmployee(int id) {
        System.out.println("Attempting to delete employee with ID: " + id);
        Employee employee = employees.remove(id);
        if (employee != null) {
            System.out.println("Employee " + employee.getFirstName() + " removed successfully.");
            BranchManager.getInstance().removeEmployeeFromBranch(employee.getBranchID());
            log("Employee " + employee.getFullName() + " deleted successfully.");
        } else {
            String s = "Employee " + id + " not found.";
            System.out.println(s);
            log(s);
        }
    }

    public void editEmployee(int employeeID, String attribute, String value)
            throws IllegalArgumentException, IllegalAccessException {
        System.out.println("Attempting to edit employee with ID: " + employeeID);

        Employee employee = employees.get(employeeID);
        if (employee == null) {
            String error = "Employee not found with ID: " + employeeID;
            System.out.println(error);
            log(error);
            throw new IllegalArgumentException("Employee not found with ID: " + employeeID);
        }

        List<Field> fields = Employee.getAllFields();
        Field field = fields.stream()
                .filter(f -> f.getName().equals(attribute))
                .findFirst().orElse(null);

        if (field == null) {
            String error = "Attribute " + attribute + " not found.";
            System.out.println(error);
            log(error);
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
            String s = "Updated attribute " + attribute + " for employee with ID: " + employeeID;
            System.out.println(s);
            log(s);
        } catch (IllegalArgumentException e) {
            String message = "The value " + value + " is not valid for field " + field.getName();
            System.out.println(message);
            log(message);
            throw new IllegalFieldValueException(message);
        }

        // Save the updated employee back to the map
        employees.remove(employeeID); //remove
        employees.put(employeeID, employee); //add to trigger onChange function
    }

    public Employee findEmployeeById(int id) {
        System.out.println("Searching for employee with ID: " + id);
        Employee employee = employees.get(id);
        log("Searching for employee with ID: " + id);
        log("Returned "+employee);
        return employee;
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
        String s = "Employees have been set successfully.";
        System.out.println(s);
        log(s);
    }

    @Override
    public void onChanged(Change<? extends Integer, ? extends Employee> change) {
        String s = "Employee changes saved.\n";
        if (change.wasAdded()) {
            String added = "Employee added: " + change.getValueAdded();
            System.out.println(added);
            s += added;
        } else if (change.wasRemoved()) {
            String removed = "Employee removed: " + change.getValueRemoved();
            System.out.println(removed);
            s += removed;
        }
        JsonUtils.saveEmployees();
        System.out.println(s);
        log(s);
    }
}
