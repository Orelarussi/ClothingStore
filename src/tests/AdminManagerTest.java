package tests;

import models.Employee;
import models.Employee.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AdminManager;
import utils.PasswordValidator;

import static org.junit.jupiter.api.Assertions.*;

public class AdminManagerTest {

    private AdminManager adminManager;

    @BeforeEach
    public void setUp() {
        adminManager = new AdminManager();
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee(1, "John", "Doe", "123456789", "", "Branch01", 12345, 1001, Position.CASHIER);
        adminManager.addEmployee(employee, "SecurePass1!");
        assertNotNull(adminManager.findEmployeeById(1));
    }

    @Test
    public void testUpdatePassword() {
        Employee employee = new Employee(1, "John", "Doe", "123456789",
                "", "Branch01", 12345, 1001, Position.CASHIER);
        adminManager.addEmployee(employee, "SecurePass1!");
        final String PASS = "NewPass123!";
        adminManager.updateEmployeePassword(1, PASS);
        final String hashed = PasswordValidator.convertToHash(PASS);
        assertTrue(adminManager.verifyEmployeePassword(1, hashed));
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee(1, "John", "Doe", "123456789", "", "Branch01", 12345, 1001, Position.CASHIER);
        adminManager.addEmployee(employee, "SecurePass1!");
        adminManager.deleteEmployee(1);
        assertNull(adminManager.findEmployeeById(1));
    }
}
