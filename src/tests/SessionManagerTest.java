//package tests;
//
//import models.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import services.SessionManager;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SessionManagerTest {
//    private SessionManager sessionManager;
//
//    @BeforeEach
//    public void setUp() {
//        sessionManager = new SessionManager();
//    }
//
//    @Test
//    public void testStartSession() {
//        User user = new User(1, "John", "Doe", "123456789", "hashedPassword");
//        sessionManager.startSession(user);
//
//        assertTrue(sessionManager.isLoggedIn(user));
//    }
//
//    @Test
//    public void testEndSession() {
//        User user = new User(1, "John", "Doe", "123456789", "hashedPassword");
//        sessionManager.startSession(user);
//        sessionManager.endSession(user);
//
//        assertFalse(sessionManager.isLoggedIn(user));
//    }
//}
