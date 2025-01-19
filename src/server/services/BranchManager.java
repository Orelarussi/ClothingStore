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
    private  Map<Integer, Branch> branches;

    // אתחול סניפים סטטיים
    private void initializeBranches() {
        Branch branch1 = new Branch(1, 0, "Tel Aviv");
        Branch branch2 = new Branch(2, 0, "Jerusalem");
        Branch branch3 = new Branch(3, 0, "Haifa");
        Branch branch4 = new Branch(4, 0, "Beersheba");

        branches.put(branch1.getBranchID(), branch1);
        branches.put(branch2.getBranchID(), branch2);
        branches.put(branch3.getBranchID(), branch3);
        branches.put(branch4.getBranchID(), branch4);
    }


    private BranchManager() {
        this.branches = new HashMap<>();
        initializeBranches();
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
public  Branch getBranchById(int branchID) {
    return branches.get(branchID);
    }

}