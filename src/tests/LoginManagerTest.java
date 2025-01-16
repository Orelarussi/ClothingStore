package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.Employee;
import server.models.Employee.Position;
import server.services.LoginManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginManagerTest {

    private LoginManager loginManager;

    @BeforeEach
    public void setUp() {
        loginManager = LoginManager.getInstance();
    }

    @Test
    public void testAddUser() {
        Employee employee = new Employee(1, 1, "Doe", "123456789",
                "hash123", "Branch001", 123456789L, Position.SHIFTMGR);
        loginManager.addUser(employee);

        // Ensure the user is added
        assertNotNull(loginManager.login(1, "hash123"));
    }

    @Test
    public void testAddDuplicateUser() {
        Employee employee1 = new Employee(3, 1, "Smith", "987654321",
                "hash456", "Branch002",
                876543210L, Position.CASHIER);
        Employee employee2 = new Employee(3, 1, "Smith", "987654321",
                "hash456", "Branch002",
                876543210L, Position.CASHIER);
        loginManager.addUser(employee1);
        loginManager.addUser(employee2);

        // Ensure only the first user is added
        assertNotNull(loginManager.login(3, "hash456"));
        assertNull(loginManager.login(3, "wrongpassword"));
    }

    @Test
    public void testLoginSuccessful() {
        Employee employee = new Employee(2, 2, "Johnson", "123123123",
                "password123", "Branch003", 654321098L, Position.CASHIER);
        loginManager.addUser(employee);

        // Test successful authentication
        assertNotNull(loginManager.login(2, "password123"));
    }

    @Test
    public void testLoginFailure() {
        Employee employee = new Employee(4, 1, "Brown", "987987987",
                "securePass", "Branch004"
                , 321098765L, Position.SELLER);
        loginManager.addUser(employee);

        // Test failed authentication with wrong password
        assertNull(loginManager.login(4, "wrongPassword"));

        // Test failed authentication with non-existent ID
        assertNull(loginManager.login(5, "securePass"));
    }

    @Test
    public void testLoginEmptyManager() {
        // Test authentication on an empty LoginManager
        assertNull(loginManager.login(1, "hash123"));
    }
}
