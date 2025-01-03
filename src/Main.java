
import models.Employee;
import models.Employee.Position;
import server.services.AdminManager;

public class Main {
    public static void main(String[] args) {
        AdminManager adminManager = new AdminManager();

        // Create employees
        Employee employee1 = new Employee(1, "Alice", "Johnson", "1234567890",
                "password123", "Branch01", 12345, Position.SHIFTMGR);
        Employee employee2 = new Employee(2, "Bob", "Smith", "0987654321",
                "papds", "Branch02", 12346002, Position.CASHIER);

        // Add employees with passwords
        adminManager.addEmployee(employee1);
        adminManager.addEmployee(employee2);

        // List employees
        adminManager.listEmployees();

        // Update password
        adminManager.updateEmployeePassword(1, "newStrongPassword1!");

        // Delete employee
        adminManager.deleteEmployee(2); // Delete employee Bob
    }
}
