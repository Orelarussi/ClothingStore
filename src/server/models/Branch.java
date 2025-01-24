package server.models;


import server.utils.JsonSerializable;

import java.util.HashMap;
import java.util.Map;

public class Branch extends JsonSerializable {
    private int branchID;
    private int employeeAmount;
    private String address;
    private Map<Integer, Integer> inventory; // Map<ProductID, Quantity>

    public Branch(int branchID, int employeeAmount, String address) {
        this.branchID = branchID;
        this.employeeAmount = employeeAmount;
        this.address = address;
        this.inventory = new HashMap<>();
    }

    public int getBranchID() {
        return branchID;
    }

    public int getEmployeeAmount() {
        return employeeAmount;
    }

    public String getAddress() {
        return address;
    }

    public Map<Integer, Integer> getInventory() {
        return inventory;
    }

    public void showInventory() {
        System.out.println("Inventory for Branch ID: " + branchID);
        inventory.forEach((productId, quantity) -> System.out.println("Product ID: " + productId + ", Quantity: " + quantity));
    }

    public void increaseEmployeeNumberBy1() {
        this.employeeAmount++;
    }
    public void reduceEmployeeNumberBy1() {
        this.employeeAmount--;
    }

    @Override
    protected void populateFromJson(String json) {
        Branch temp = gson.fromJson(json, Branch.class);

        this.branchID = temp.getBranchID();
        this.employeeAmount = temp.getEmployeeAmount();
        this.address = temp.getAddress();
        this.inventory = temp.getInventory();
    }
}
