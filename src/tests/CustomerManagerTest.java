package tests;

import models.customer.Customer;
import models.customer.NewCustomer;
import models.customer.ReturningCustomer;
import models.customer.VIPCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.CustomerManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerManagerTest {
    private CustomerManager customerManager;

    @BeforeEach
    public void setUp() {
        customerManager = new CustomerManager();
    }

    @Test
    public void testAddVIPCustomer() {
        Customer vipCustomer = new VIPCustomer(1, "John", "Doe", "123456789", "Branch01");
        customerManager.addCustomer(vipCustomer);

        List<Customer> customers = customerManager.getAllCustomers();
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getFirstName());
        assertTrue(customers.get(0) instanceof VIPCustomer);
        assertEquals("VIP customer: 20% off on all purchases.", customers.get(0).getPurchasePlan().getPlanDetails());
    }

    @Test
    public void testAddNewCustomer() {
        Customer newCustomer = new NewCustomer(2, "Alice", "Smith", "987654321", "Branch02");
        customerManager.addCustomer(newCustomer);

        List<Customer> customers = customerManager.getAllCustomers();
        assertEquals(1, customers.size());
        assertEquals("Alice", customers.get(0).getFirstName());
        assertTrue(customers.get(0) instanceof NewCustomer);
        assertEquals("New customer: 10% off on first purchase.", customers.get(0).getPurchasePlan().getPlanDetails());
    }

    @Test
    public void testAddReturningCustomer() {
        Customer returningCustomer = new ReturningCustomer(3, "Bob", "Johnson", "555123456", "Branch03");
        customerManager.addCustomer(returningCustomer);

        List<Customer> customers = customerManager.getAllCustomers();
        assertEquals(1, customers.size());
        assertEquals("Bob", customers.get(0).getFirstName());
        assertTrue(customers.get(0) instanceof ReturningCustomer);
        assertEquals("Returning customer: 15% off after 3 purchases.", customers.get(0).getPurchasePlan().getPlanDetails());
    }

    @Test
    public void testFindCustomerById() {
        Customer vipCustomer = new VIPCustomer(1, "John", "Doe", "123456789", "Branch01");
        customerManager.addCustomer(vipCustomer);

        Customer found = customerManager.findCustomerById(1);
        assertNotNull(found);
        assertTrue(found instanceof VIPCustomer);
        assertEquals("John", found.getFirstName());
    }

    @Test
    public void testDeleteCustomer() {
        Customer returningCustomer = new ReturningCustomer(3, "Bob", "Johnson", "555123456", "Branch03");
        customerManager.addCustomer(returningCustomer);

        customerManager.deleteCustomer(3);
        Customer deleted = customerManager.findCustomerById(3);
        assertNull(deleted, "Customer should be deleted successfully.");
    }

    @Test
    public void testGetAllCustomers() {
        Customer vipCustomer = new VIPCustomer(1, "John", "Doe", "123456789", "Branch01");
        Customer newCustomer = new NewCustomer(2, "Alice", "Smith", "987654321", "Branch02");

        customerManager.addCustomer(vipCustomer);
        customerManager.addCustomer(newCustomer);

        List<Customer> customers = customerManager.getAllCustomers();
        assertEquals(2, customers.size());
        assertTrue(customers.contains(vipCustomer));
        assertTrue(customers.contains(newCustomer));
    }
}
