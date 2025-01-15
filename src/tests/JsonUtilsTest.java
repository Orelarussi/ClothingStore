package tests;

import org.junit.jupiter.api.Test;
import server.models.Branch;
import server.models.Employee;
import server.models.customer.*;
import server.services.AdminManager;
import server.services.BranchManager;
import server.services.EmployeeManager;
import server.utils.JsonUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void testSave() {
//        Add employees
        AdminManager manager = AdminManager.getInstance();
        BranchManager.getInstance().addBranch(new Branch(222, "Holon"));

        Employee employee1 = new Employee(2, 222, "fname", "lname", "3345634674567", "12112",
                222222, Employee.Position.CASHIER);
        manager.addEmployee(employee1);

        Employee employee2 = new Employee(3, 222, "fname", "lname", "56785346", "436457",
                85796898, Employee.Position.SELLER);
        manager.addEmployee(employee2);
//        Add customers
        NewCustomer nc = new NewCustomer(1,"new","last","23345",222);
        ReturningCustomer rc = new ReturningCustomer(2,"returned","last","6581651",222);
        VIPCustomer vc = new VIPCustomer(3,"vip","last","651666",222);

        EmployeeManager.getInstance().setCustomers(List.of(nc, rc, vc));

        JsonUtils.save();
        String path = JsonUtils.Files.Employees.getFileName();
        File file = new File(path);
        assertTrue(file.exists());

        path = JsonUtils.Files.Customers.getFileName();
        file = new File(path);
        assertTrue(file.exists());


//        file.delete();
    }

    @Test
    void testLoad() {
        String path = JsonUtils.Files.Employees.getFileName();
        File file = new File(path);

        testSave();
        JsonUtils.load();
        assertTrue(file.exists());

        List<Employee> employees = AdminManager.getInstance().getAllEmployees();

        assertNotNull(employees);

        assertEquals(2, employees.size());

        Employee employee = employees.get(0);

        assertEquals(2, employee.getId());

        employee = employees.get(1);

        assertEquals(3, employee.getId());

        Map<Integer, Customer> customersMap = EmployeeManager.getInstance().getCustomers();

        assertNotNull(customersMap);
        assertEquals(3, customersMap.size());

        assertTrue(customersMap.containsKey(1));
        assertInstanceOf(NewCustomer.class, customersMap.get(1));
        assertInstanceOf(ReturningCustomer.class, customersMap.get(2));
        assertInstanceOf(VIPCustomer.class, customersMap.get(3));

//        file.delete();
    }
}