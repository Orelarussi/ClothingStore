package server.models;

import server.exceptions.InvalidQuantityException;
import server.utils.JsonSerializable;

public class Product extends JsonSerializable {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String branch;

    public Product(int id, String name, String category, double price, int quantity,String branch) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.branch = branch;
    }
    public Product(String jsonString){
        populateFromJson(jsonString);
    }


    @Override
    protected void populateFromJson(String json) {
        Product temp = gson.fromJson(json,Product.class);
        this.id = temp.id;
        this.name = temp.name;
        this.category = temp.category;
        this.price = temp.price;
        this.quantity = temp.quantity;
        this.branch = temp.branch;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative for product: " + name);
        }
        this.quantity = quantity;
    }

    public String getBranch(){return branch;}
    public void setBranch(String branch){
        this.branch = branch;
    }
}