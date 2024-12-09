package models;

public class Customer extends User {
    private String type;
    private String branchID;

    public Customer(int id, String firstName, String lastName, String phoneNumber, String passwordHash, String type, String branchID) {
        super(id, firstName, lastName, phoneNumber, passwordHash);
        this.type = type;
        this.branchID = branchID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
