package server.services;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
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
        return branches.get(branchID);
    }

    public ObservableMap<Integer, Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branchList) {
        ObservableMap<Integer, Branch> map = FXCollections.observableHashMap();
        branchList.forEach(branch -> map.put(branch.getBranchID(), branch));
        if (this.branches != null) {
            this.branches.removeListener(this);
        }
        this.branches = map;
        this.branches.addListener(this);
    }

    public void addEmployeeToBranch(int branchID) {
        Branch b = branches.get(branchID);
        if (b == null) {
            throw new IllegalArgumentException("Branch with id " + branchID+ " doesn't exist!");
        }
        b.increaseEmployeeNumberBy1();
        JsonUtils.saveBranches();
        System.out.println("Updated branches employees successfully");
    }

    public void removeEmployeeFromBranch(int branchID) {
        Branch b = branches.get(branchID);
        if (b == null) {
            throw new IllegalArgumentException("Branch with id" + branchID+ " doesn't exist!");
        }
        b.reduceEmployeeNumberBy1();
        JsonUtils.saveBranches();
        System.out.println("Updated branches successfully");
    }

    @Override
    public void onChanged(Change change) {
        System.out.println("Branch change detected: " + change);
        JsonUtils.saveBranches();
    }
}