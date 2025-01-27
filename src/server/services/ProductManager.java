package server.services;

import server.models.Product;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductManager {
    private static ProductManager instance;

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    private Map<Integer, Product> products;

    public ProductManager() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Product getProduct(int productId) {
        System.out.println("Fetching product with ID: " + productId);
        Product product = products.get(productId);
        if (product != null) {
            System.out.println("Product found: " + product.getName());
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
        return product;
    }

    public Map<Integer, Product> getProducts() {
        System.out.println("Fetching all products.");
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products.stream()
                .map(product -> new AbstractMap.SimpleEntry<>(product.getId(), product))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Products list set successfully.");
    }

    public void displayProducts() {
        System.out.println("Displaying all products:");
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            for (Product product : products.values()) {
                System.out.println("ID: " + product.getId() + ", Name: " + product.getName() +
                        ", Category: " + product.getCategory() + ", Price: $" + product.getPrice());
            }
        }
    }
}


