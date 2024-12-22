package tests;

import models.Employee;
import models.Employee.Position; // Ensure to import the Position enum
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.EmployeeManager;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeManagerTest {

    private EmployeeManager employeeManager;

    @BeforeEach
    public void setUp() {
        employeeManager = new EmployeeManager();
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee(1, "John", "Doe", "123456789", "password123", "Branch001", 123456789L, 987654321L, Position.SHIFTMGR);
        employeeManager.addEmployee(employee, "password123");

        // Ensure the employee is added
        assertNotNull(employeeManager.findEmployeeById(1));
        assertEquals("John", employeeManager.findEmployeeById(1).getFirstName());
    }

    @Test
    public void testVerifyPassword() {
        Employee employee = new Employee(2, "Jane", "Smith", "987654321", "securePassword", "Branch002", 234567890L, 876543210L, Position.CASHIER);
        employeeManager.addEmployee(employee, "securePassword");

        // Verify the password is correct
        assertTrue(employeeManager.verifyPassword(employee, "securePassword"));

        // Verify the password is incorrect
        assertFalse(employeeManager.verifyPassword(employee, "wrongPassword"));
    }

    @Test
    public void testUpdatePassword() {
        Employee employee = new Employee(3, "Alice", "Johnson", "456789123", "oldPassword", "Branch003", 345678901L, 654321098L, Position.SELLER);
        employeeManager.addEmployee(employee, "oldPassword");

        // Update the password
        employeeManager.updatePassword(3, "newPassword");

        // Verify the new password works
        assertTrue(employeeManager.verifyPassword(employee, "newPassword"));

        // Verify the old password no longer works
        assertFalse(employeeManager.verifyPassword(employee, "oldPassword"));
    }

    @Test
    public void testFindEmployeeById() {
        Employee employee = new Employee(4, "Bob", "Brown", "789456123", "anotherPassword", "Branch004", 456789012L, 321098765L, Position.CASHIER);
        employeeManager.addEmployee(employee, "anotherPassword");

        // Find employee by ID
        Employee foundEmployee = employeeManager.findEmployeeById(4);
        assertNotNull(foundEmployee);
        assertEquals("Bob", foundEmployee.getFirstName());
    }

    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee(5, "Emma", "Wilson", "321654987", "password1", "Branch005", 567890123L, 210987654L, Position.SELLER);
        Employee employee2 = new Employee(6, "Liam", "Davis", "654987321", "password2", "Branch006", 678901234L, 109876543L, Position.SHIFTMGR);

        employeeManager.addEmployee(employee1, "password1");
        employeeManager.addEmployee(employee2, "password2");

        // Get all employees
        assertEquals(2, employeeManager.getAllEmployees().size());
    }
}
