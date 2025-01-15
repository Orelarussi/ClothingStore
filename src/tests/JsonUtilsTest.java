package tests;

import org.junit.jupiter.api.Test;
import server.models.Branch;
import server.models.Employee;
import server.services.AdminManager;
import server.services.BranchManager;
import server.utils.JsonUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void save() {
        AdminManager manager = AdminManager.getInstance();
        BranchManager.getInstance().addBranch(new Branch(222, "Holon"));

        Employee employee1 = new Employee(2, 222, "fname", "lname", "3345634674567", "12112",
                222222, Employee.Position.CASHIER);
        manager.addEmployee(employee1);

        Employee employee2 = new Employee(3, 222, "fname", "lname", "56785346", "436457",
                85796898, Employee.Position.SELLER);
        manager.addEmployee(employee2);

        JsonUtils.save();
        String path = JsonUtils.Files.Employees.getFileName();
        File file = new File(path);
        assertTrue(file.exists());
//        file.delete();
    }

    @Test
    void load() {
        save();
        JsonUtils.load();
        String path = JsonUtils.Files.Employees.getFileName();
        File file = new File(path);
        assertTrue(file.exists());

        List<Employee> employees = AdminManager.getInstance().getAllEmployees();

        assertNotNull(employees);

        assertEquals(2, employees.size());

        Employee employee = employees.get(0);

        assertEquals(2, employee.getId());

        employee = employees.get(1);

        assertEquals(3, employee.getId());

//        file.delete();
    }
}