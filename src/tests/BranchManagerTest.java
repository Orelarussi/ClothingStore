package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.Branch;
import server.services.BranchManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BranchManagerTest {

    private BranchManager branchManager;

    @BeforeEach
    public void setUp() {
        branchManager = BranchManager.getInstance();
        branchManager.initializeBranches();
    }

    @Test
    public void testShowInventoryByBranchId() {
        Branch branch = branchManager.getBranchById(1);
        assertNotNull(branch);
        branch.updateInventory(101, 50);
        assertEquals(50, branch.getInventory().get(101));
    }

    @Test
    public void testAddProductToBranch() {
        Branch branch = branchManager.getBranchById(1);
        assertNotNull(branch);
        branch.updateInventory(102, 30);
        assertEquals(30, branch.getInventory().get(102));
    }

    @Test
    public void testShowEmployeesByBranchId() {
        Branch branch = branchManager.getBranchById(1);
        branchManager.addEmployeeToBranch(1);
        branchManager.addEmployeeToBranch(1);
        assertEquals(2, branch.getEmployeeAmount());
    }
}
