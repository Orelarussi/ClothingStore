package models.customer;

public class Customer extends User {
    private Type type;
    private String branchID;
    protected PurchasePlan purchasePlan;

    public Customer(int id, String firstName, String lastName, String phoneNumber, String branchID, String passwordHash) {
        super(id, firstName, lastName, phoneNumber, passwordHash);
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

    @Override
    public String toString() {
        return super.toString() + "Customer{" +
                "type='" + type + '\'' +
                ", branchID='" + branchID + '\'' +
                '}';
    }
}
