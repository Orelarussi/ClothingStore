package server.services;

import server.models.Branch;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

public  Branch getBranchById(int branchID) {
    return branches.get(branchID);
    }

    public Map<Integer, Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches.stream()
                .map(branch -> new AbstractMap.SimpleEntry<>(branch.getBranchID(), branch))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}