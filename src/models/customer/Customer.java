package models.customer;

import models.User;
import models.purchase_plan.PurchasePlan;

public abstract class Customer extends User {

    private String branchID;
    protected PurchasePlan purchasePlan;

    public Customer(int id, String firstName, String lastName, String phoneNumber, String branchID, String passwordHash) {
        super(id, firstName, lastName, phoneNumber);
        this.branchID = branchID;
        this.purchasePlan = createPurchasePlan();
    }

    protected abstract PurchasePlan createPurchasePlan();

    public Customer(int id, String firstName, String lastName, String phoneNumber, String branchID) {
        this(id, firstName, lastName, phoneNumber, branchID, null);
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public PurchasePlan getPurchasePlan() {
        return purchasePlan;
    }

    public void setPurchasePlan(PurchasePlan purchasePlan) {
        this.purchasePlan = purchasePlan;
    }

    @Override
    public String toString() {
        return super.toString() + "Customer{" +
                ", branchID='" + branchID + '\'' +
                ", plan='" + purchasePlan +'\'' +
                '}';
    }
}
