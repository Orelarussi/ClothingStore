
import models.Employee;
import models.Employee.Position;
import services.AdminManager;

public class Main {
    public static void main(String[] args) {
        AdminManager adminManager = new AdminManager();

        // Create employees
        Employee employee1 = new Employee(1, "Alice", "Johnson", "1234567890", "", "Branch01", 12345, 1001, Position.SHIFTMGR);
        Employee employee2 = new Employee(2, "Bob", "Smith", "0987654321", "", "Branch02", 12346, 1002, Position.CASHIER);

        // Add employees with passwords
        adminManager.addEmployee(employee1, "password123"); // Invalid password
        adminManager.addEmployee(employee2, "weakpass");    // Invalid password

        // List employees
        adminManager.listEmployees();

        // Update password
        adminManager.updateEmployeePassword(1, "newStrongPassword1!");  // Valid password
        adminManager.updateEmployeePassword(1, "short");                // Invalid password

        // Delete employee
        adminManager.deleteEmployee(2); // Delete employee Bob
    }
}
