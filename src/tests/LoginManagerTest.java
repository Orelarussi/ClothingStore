package tests;

import server.models.Employee;
import server.models.Employee.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.LoginManager;

import static org.junit.jupiter.api.Assertions.*;

public class LoginManagerTest {

    private LoginManager loginManager;

    @BeforeEach
    public void setUp() {
        loginManager = new LoginManager();
    }

    @Test
    public void testAddUser() {
        Employee employee = new Employee(1, "John", "Doe", "123456789",
                "hash123", "Branch001", 123456789L, Position.SHIFTMGR);
        loginManager.addUser(employee);

        // Ensure the user is added
        assertTrue(loginManager.login(1, "hash123"));
    }

    @Test
    public void testAddDuplicateUser() {
        Employee employee1 = new Employee(3, "Jane", "Smith", "987654321",
                "hash456", "Branch002",
                876543210L, Position.CASHIER);
        Employee employee2 = new Employee(3, "Jane", "Smith", "987654321",
                "hash456", "Branch002",
                876543210L, Position.CASHIER);
        loginManager.addUser(employee1);
        loginManager.addUser(employee2);

        // Ensure only the first user is added
        assertTrue(loginManager.login(3, "hash456"));
        assertFalse(loginManager.login(3, "wrongpassword"));
    }

    @Test
    public void testLoginSuccessful() {
        Employee employee = new Employee(2, "Alice", "Johnson", "123123123",
                "password123", "Branch003", 654321098L, Position.CASHIER);
        loginManager.addUser(employee);

        // Test successful authentication
        assertTrue(loginManager.login(2, "password123"));
    }

    @Test
    public void testLoginFailure() {
        Employee employee = new Employee(4, "Bob", "Brown", "987987987",
                "securePass", "Branch004"
                , 321098765L, Position.SELLER);
        loginManager.addUser(employee);

        // Test failed authentication with wrong password
        assertFalse(loginManager.login(4, "wrongPassword"));

        // Test failed authentication with non-existent ID
        assertFalse(loginManager.login(5, "securePass"));
    }

    @Test
    public void testLoginEmptyManager() {
        // Test authentication on an empty LoginManager
        assertFalse(loginManager.login(1, "hash123"));
    }
}
