//package tests;
//
//import models.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import services.UserManager;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserManagerTest {
//    private UserManager userManager;
//
//    @BeforeEach
//    public void setUp() {
//        userManager = new UserManager();
//    }
//
//    @Test
//    public void testAddUser() {
//        User user = new User(1, "John", "Doe", "123456789", "hashedPassword");
//        userManager.addUser(user);
//
//        assertNotNull(userManager.findUserById(1));
//    }
//
//    @Test
//    public void testGetAllUsers() {
//        User user1 = new User(1, "John", "Doe", "123456789", "hashedPassword");
//        User user2 = new User(2, "Alice", "Smith", "987654321", "hashedPassword2");
//
//        userManager.addUser(user1);
//        userManager.addUser(user2);
//
//        List<User> users = userManager.getAllUsers();
//        assertEquals(2, users.size());
//    }
//}
