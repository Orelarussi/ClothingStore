package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.customer.Customer;
import server.models.customer.NewCustomer;
import server.services.EmployeeManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeManagerTest {

    private EmployeeManager employeeManager;

    @BeforeEach
    public void setUp() {
        employeeManager = EmployeeManager.getInstance();
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new NewCustomer(1, "John", "Doe", "123456789", 0);
        assertTrue(employeeManager.addCustomer(customer));
        assertNotNull(employeeManager.getCustomers().get(1));
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new NewCustomer(1, "John", "Doe", "123456789", 0);
        employeeManager.addCustomer(customer);
        assertTrue(employeeManager.deleteCustomer(1));
        assertNull(employeeManager.getCustomers().get(1));
    }

    @Test
    public void testSetCustomers() {
        Customer customer1 = new NewCustomer(1, "John", "Doe", "123456789", 0);
        Customer customer2 = new NewCustomer(2, "Jane", "Smith", "987654321", 0);
        employeeManager.setCustomers(List.of(customer1, customer2));
        assertEquals(2, employeeManager.getCustomers().size());
    }
}
