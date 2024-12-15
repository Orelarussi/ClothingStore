package tests;

import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.LoginManager;

import static org.junit.jupiter.api.Assertions.*;

public class LoginManagerTest {
    private LoginManager loginManager;

    @BeforeEach
    public void setUp() {
        loginManager = new LoginManager();
    }

    @Test
    public void testSuccessfulLogin() {
        User user = new User(1, "John", "Doe", "123456789", "hashedPassword");
        loginManager.addUser(user);

        assertTrue(loginManager.authenticate(123456789, "hashedPassword"));
    }

    @Test
    public void testFailedLogin() {
        User user = new User(1, "John", "Doe", "123456789", "hashedPassword");
        loginManager.addUser(user);

        assertFalse(loginManager.authenticate(123456789, "wrongPassword"));
    }
}
