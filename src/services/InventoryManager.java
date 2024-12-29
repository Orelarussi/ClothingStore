package services;

import models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    private List<Product> products;

    public InventoryManager() {
        this.products = new ArrayList<>();
    }

    // Add a new product
    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            System.out.println("Product added: " + product.getName());
        } else System.out.println("Product already exists");
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            System.out.println("Product removed: " + product.getName());
        } else {
            System.out.println("Product not found: " + product.getName());
        }
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
        return products; // Return a copy of the product list to avoid modifications
    }

    public void updateItem(Product product) {
        for (int i = 0; i < products.size(); i++) {
            Product temp = products.get(i);
            if (temp.getId() == product.getId()) {
                products.set(i, product);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found with ID: " + product.getId());
    }

    public List<Product> getProductsByBranch(String branchID) {
        return products.stream().filter(product -> product.getBranch().equals(branchID)).toList();
    }

    public boolean removeProduct(int productId) {
        return products.removeIf(product -> product.getId() == productId);
    }
}
