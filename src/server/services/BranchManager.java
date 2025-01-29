package server.services;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import server.logger.Logger;
import server.models.Branch;
import server.utils.JsonUtils;

import java.util.List;

public class BranchManager implements MapChangeListener<Integer, Branch> {
    // singleton
    private static BranchManager instance;

    public static synchronized BranchManager getInstance() {
        if (instance == null) {
            instance = new BranchManager();
        }
        return instance;
    }

    private ObservableMap<Integer, Branch> branches;

    private BranchManager() {
        this.branches = FXCollections.observableHashMap();
    }

    public void initializeBranches() {
        Branch branch1 = new Branch(1, 0, "Tel Aviv");
        Branch branch2 = new Branch(2, 0, "Jerusalem");
        Branch branch3 = new Branch(3, 0, "Haifa");
        Branch branch4 = new Branch(4, 0, "Beersheba");

        branches.put(branch1.getBranchID(), branch1);
        branches.put(branch2.getBranchID(), branch2);
        branches.put(branch3.getBranchID(), branch3);
        branches.put(branch4.getBranchID(), branch4);

        JsonUtils.saveBranches();
    }

    public void addBranch(Branch branch) {
        branches.put(branch.getBranchID(), branch);
    }

    public Branch getBranchById(int branchID) {
        System.out.println("Fetching branch with ID: " + branchID);
        return branches.get(branchID);
    }

    public ObservableMap<Integer, Branch> getBranches() {
        System.out.println("Fetching all branches.");
        return branches;
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
    public void setBranches(List<Branch> branchList) {
        System.out.println("Setting branches.");
        ObservableMap<Integer, Branch> map = FXCollections.observableHashMap();
        branchList.forEach(branch -> map.put(branch.getBranchID(), branch));
        if (this.branches != null) {
            this.branches.removeListener(this);
        }
        this.branches = map;
        this.branches.addListener(this);
        System.out.println("Branches have been set successfully.");
    }

    public void addEmployeeToBranch(int branchID) {
        Branch b = branches.get(branchID);
        if (b == null) {
            String error = "Branch with ID " + branchID + " doesn't exist!";
            System.out.println(error);
            throw new IllegalArgumentException("Branch with id" + branchID+ " doesn't exist!");
        }
        b.increaseEmployeeNumberBy1();
        JsonUtils.saveBranches();
        System.out.println("Updated branches employees successfully");
    }

    public void addProductToBranch(int branchID , int productToAddId , int amountToAdd) {
        Branch branch = branches.get(branchID);
        if (branch == null) {
            String error = "Branch with ID " + branchID + " doesn't exist!";
            System.out.println(error);
            throw new IllegalArgumentException("Branch with id" + branchID+ " doesn't exist!");
        }
        branch.updateProduct(productToAddId,amountToAdd);

        JsonUtils.saveBranches();
        System.out.println("Updated branches employees successfully");
    }

    public void removeEmployeeFromBranch(int branchID) {
        Branch b = branches.get(branchID);
        if (b == null) {
            String error = "Branch with ID " + branchID + " doesn't exist!";
            System.out.println(error);
            throw new IllegalArgumentException("Branch with id" + branchID+ " doesn't exist!");
        }
        b.reduceEmployeeNumberBy1();
        JsonUtils.saveBranches();
        System.out.println("Updated branches employees successfully");
    }

    @Override
    public void onChanged(Change change) {
        String s1 = "Branch change detected: " + change;
        System.out.println(s1);
        Logger.log(s1);
        JsonUtils.saveBranches();
        System.out.println("Branch changes saved.");
    }
}