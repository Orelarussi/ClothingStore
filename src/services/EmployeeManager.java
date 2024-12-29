package services;

import client.serverCommunication.Format;
import models.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.Server;

public class EmployeeManager {
    private Map<Integer, Employee> employees;

    public EmployeeManager() {
        this.employees = new HashMap<>();
    }

    // Add a new employee with a hashed password
    public void addEmployee(Employee employee) {
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

    public void updateEmployee(Employee emp) {
        employees.put(emp.getId(), emp);
        System.out.println("Employee updated successfully for employee: " + emp.getFirstName());
    }

    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    public List<Employee> getEmployeesByBranch(String branchID) {
        List<Employee> employees = getAllEmployees();
        List<Employee> filtered = employees.stream().filter(emp-> emp.getBranchID().equals(branchID)).toList();
        return filtered;
    }
    public String login(String username, String password) throws SQLException
    {
        String response;
        Employee emp = getEmployeeByID(Integer.parseInt(username));
        if(emp == null)
            response = Format.encodeException("לא קיים משתמש כזה במערכת");
        else if(!emp.getPassword().equals(password))
            response = Format.encodeException("הסיסמה שהכנסת שגויה");
        else if(Server.getSocketDataByEmployee(emp) != null)
            response = Format.encodeException("המשתמש כבר מחובר למערכת");
        else
            response = emp.serializeToString();
        return response;
    }

    public Employee getEmployeeByID(int id)
    {
        return employees.get(id);
    }
}
