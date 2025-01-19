package server.services;

import server.models.Product;
import server.utils.JsonUtils;

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
        return products.get(productId);
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products.stream()
                .map(product -> new AbstractMap.SimpleEntry<>(product.getId(), product))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void displayProducts() {
        System.out.println("Product List:");
        for (Product product : products.values()) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Category: " + product.getCategory() + ", Price: $" + product.getPrice());
        }
    }
}


