package models;

import server.services.InventoryManager;

public class Branch {
    private String id;
    private String name;
    private InventoryManager inventoryManager;

    public Branch(String id, String name) {
        this.id = id;
        this.name = name;
        this.inventoryManager = new InventoryManager();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
