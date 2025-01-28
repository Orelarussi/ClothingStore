package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.Branch;
import server.models.Product;
import server.services.BranchManager;
import server.services.ProductManager;

import static org.junit.jupiter.api.Assertions.*;

public class ProductManagerTest {

    private ProductManager productManager;
    private BranchManager branchManager;

    @BeforeEach
    public void setUp() {
        productManager = ProductManager.getInstance();
        branchManager = BranchManager.getInstance();
        branchManager.initializeBranches();
    }

    @Test
    public void testAddProductToBranch() {
        Branch branch = branchManager.getBranchById(1);
        Product product = new Product(1, "Shirt", "Clothing", 19.99);
        productManager.addProduct(product);
        branch.updateInventory(product.getId(), 10); // Add 10 shirts to inventory
        assertEquals(10, branch.getInventory().get(product.getId()));
    }
}
