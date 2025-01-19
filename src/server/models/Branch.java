package server.models;


import server.utils.JsonSerializable;

import java.util.HashMap;
import java.util.Map;

public class Branch extends JsonSerializable {
    private int branchID;
    private int employeeAmount;
    private String address;
    private Map<Integer, Integer> inventory; // Map<ProductID, Quantity>
    private Map<Integer, Integer> sales; // Map<ProductID, Sales Amount>

    public Branch(int branchID, int employeeAmount, String address) {
        this.branchID = branchID;
        this.employeeAmount = employeeAmount;
        this.address = address;
        this.inventory = new HashMap<>();
        this.sales = new HashMap<>();
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

    public Map<Integer, Integer> getSales() {
        return sales;
    }

    public void updateInventory(int productId, int quantity) {
        inventory.put(productId, inventory.getOrDefault(productId, 0) + quantity);
    }

    public void updateSales(int productId, int amount) {
        sales.put(productId, sales.getOrDefault(productId, 0) + amount);
    }

    public int showSalesAmountByBranch() {
        return sales.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int showSalesAmountByProductID(int productId) {
        return sales.getOrDefault(productId, 0);
    }

    public void showInventory() {
        System.out.println("Inventory for Branch ID: " + branchID);
        inventory.forEach((productId, quantity) -> System.out.println("Product ID: " + productId + ", Quantity: " + quantity));
    }

    public void increaseEmployeNumberBy1() {
        this.employeeAmount++;
    }
    public void ReduceEmployeNumberBy1() {
        this.employeeAmount--;
    }

    @Override
    protected void populateFromJson(String json) {
        Branch temp = gson.fromJson(json, Branch.class);

        this.branchID = temp.getBranchID();
        this.employeeAmount = temp.getEmployeeAmount();
        this.address = temp.getAddress();
        this.inventory = temp.getInventory();
        this.sales = temp.getSales();
    }
}
