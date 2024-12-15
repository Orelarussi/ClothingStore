package services;

import models.Product;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Product> products;

    public InventoryManager() {
        this.products = new ArrayList<>();
    }

    // Add a new product
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName());
    }

    // Update product quantity
    public void updateProductQuantity(int productId, int quantityChange) {
        for (Product product : products) {
            if (product.getId() == productId) {
                int newQuantity = product.getQuantity() + quantityChange;
                if (newQuantity < 0) {
                    throw new IllegalArgumentException("Quantity cannot be negative for product: " + product.getName());
                }
                product.setQuantity(newQuantity);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found with ID: " + productId);
    }



    // Display inventory
    public void displayInventory() {
        System.out.println("Inventory:");
        for (Product product : products) {
            System.out.println(product.getName() + " - Quantity: " + product.getQuantity());
        }
    }
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // Return a copy of the product list to avoid modifications
    }
}
