package server.services;

import server.models.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchManager {
    // singleton
    private static BranchManager instance;

    public static synchronized BranchManager getInstance() {
        if (instance == null) {
            instance = new BranchManager();
        }
        return instance;
    }

    private final Map<Integer, Branch> branches;

    public BranchManager() {
        this.branches = new HashMap<>();
    }

    public void addBranch(Branch branch) {
        branches.put(branch.getBranchID(), branch);
    }


    public void updateInventory(int branchID, int productId, int quantity) {
        Branch branch = branches.get(branchID);
        if (branch != null) {
            branch.updateInventory(productId, quantity);
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void updateSales(int branchID, int productId, int amount) {
        Branch branch = branches.get(branchID);
        if (branch != null) {
            branch.updateSales(productId, amount);
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void showSalesAmountByBranch(int branchID) {
        Branch branch = branches.get(branchID);
        if (branch != null) {
            System.out.println("Total Sales for Branch ID " + branchID + ": " + branch.showSalesAmountByBranch());
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void showSalesAmountByProductID(int branchID, int productId) {
        Branch branch = branches.get(branchID);
        if (branch != null) {
            System.out.println("Sales for Product ID " + productId + " in Branch ID " + branchID + ": " + branch.showSalesAmountByProductID(productId));
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void showInventory(int branchID) {
        Branch branch = branches.get(branchID);
        if (branch != null) {
            branch.showInventory();
        } else {
            System.out.println("Branch not found.");
        }
    }

    public Branch getBranchById(int branchID) {
        return branches.get(branchID);
    }

}