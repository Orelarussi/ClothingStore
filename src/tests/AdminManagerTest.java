package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.Branch;
import server.models.Employee;
import server.models.Employee.Position;
import server.services.AdminManager;
import server.services.BranchManager;

import static org.junit.jupiter.api.Assertions.*;

public class AdminManagerTest {

    private AdminManager adminManager;
    private BranchManager branchManager;

    @BeforeEach
    public void setUp() {
        adminManager = AdminManager.getInstance();
        branchManager = BranchManager.getInstance();

        // Ensure Branch ID 1 exists before tests
        Branch branch = new Branch(1, 0, "Branch01");
        branchManager.addBranch(branch);
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee(154, 1, "Doe", "123456789",
                "SecurePass1!", "Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        assertNotNull(adminManager.findEmployeeById(154));
    }

    @Test
    public void testUpdatePassword() {
        Employee employee = new Employee(155, 1, "Doe", "123456789",
                "SecurePass1!", "Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        final String PASS = "NewPass123!";
        try {
            adminManager.editEmployee(155, "password", PASS);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertTrue(adminManager.verifyEmployeePassword(155, PASS));
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee(156, 1, "Doe", "123456789",
                "SecurePass1!", "Branch01", 12345, Position.CASHIER);
        adminManager.addEmployee(employee);
        adminManager.deleteEmployee(156);
        assertNull(adminManager.findEmployeeById(156));
    }
}
