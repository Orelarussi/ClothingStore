package tests;

import models.Employee;
import models.Employee.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.AdminManager;

import static org.junit.jupiter.api.Assertions.*;

public class AdminManagerTest {

    private AdminManager adminManager;

    @BeforeEach
    public void setUp() {
        adminManager = new AdminManager();
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee(1, "John", "Doe", "123456789",
                "SecurePass1!","Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        assertNotNull(adminManager.findEmployeeById(1));
    }

    @Test
    public void testUpdatePassword() {
        Employee employee = new Employee(1, "John", "Doe", "123456789",
                "SecurePass1!", "Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        final String PASS = "NewPass123!";
        adminManager.updateEmployeePassword(1, PASS);
        assertTrue(adminManager.verifyEmployeePassword(1, PASS));
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee(1, "John", "Doe", "123456789",
                "SecurePass1!", "Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        adminManager.deleteEmployee(1);
        assertNull(adminManager.findEmployeeById(1));
    }
}
