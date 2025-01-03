package tests;
import server.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.InventoryManager;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagerTest {
    private InventoryManager inventoryManager;

    @BeforeEach
    public void setUp() {
        inventoryManager = new InventoryManager();
    }

    @Test
    public void testAddProduct() {
        Product product = new Product(1, "T-Shirt", "Clothing", 19.99, 50,"holon");
        inventoryManager.addProduct(product);

        assertEquals(1, inventoryManager.getAllProducts().size());
    }

    @Test
    public void testUpdateProductQuantity() {
        Product product = new Product(1, "T-Shirt", "Clothing", 19.99, 50,"holon");
        inventoryManager.addProduct(product);

        inventoryManager.updateProductQuantity(1, 20);
        assertEquals(70, product.getQuantity());
    }

    @Test
    public void testRemoveProduct() {
        Product product = new Product(1, "T-Shirt", "Clothing", 19.99, 50,"tlv");
        inventoryManager.addProduct(product);

        // Ensure the product is added
        assertEquals(1, inventoryManager.getAllProducts().size());

        // Remove the product
        inventoryManager.removeProduct(product);

        // Verify the product is removed
        assertEquals(0, inventoryManager.getAllProducts().size());
    }


    @Test
    public void testProductNotFound() {
        assertThrows(IllegalArgumentException.class, () -> inventoryManager.updateProductQuantity(99, 10));
    }
}
