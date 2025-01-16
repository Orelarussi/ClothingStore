package server.services;

import server.models.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private static ProductManager instance;

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    private final Map<Integer, Product> products;

    public ProductManager() {
        this.products = new HashMap<>();
        products.put(1, new Product(1, "Skinny Jeans", "Jeans", 39.99));
        products.put(2, new Product(2, "Relaxed Jeans", "Jeans", 49.99));
        products.put(3, new Product(3, "Leather Jacket", "Jackets", 89.99));
        products.put(4, new Product(4, "Denim Jacket", "Jackets", 79.99));
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }

    public void displayProducts() {
        System.out.println("Product List:");
        for (Product product : products.values()) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Category: " + product.getCategory() + ", Price: $" + product.getPrice());
        }
    }
}


