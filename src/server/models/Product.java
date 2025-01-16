package server.models;

import server.utils.JsonSerializable;

public class Product extends JsonSerializable {
    private int id;
    private String name;
    private String category;
    private double price;

    public Product(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }


    @Override
    protected void populateFromJson(String json) {
        Product temp = gson.fromJson(json, Product.class);
        this.id = temp.id;
        this.name = temp.name;
        this.category = temp.category;
        this.price = temp.price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}