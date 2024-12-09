package models;

public class Customer extends User {
    private Type type;
    private String branchID;

    public enum Type { VIP,NEW,RETURNING}

    public Customer(int id, String firstName, String lastName, String phoneNumber, String passwordHash, Type type, String branchID) {
        super(id, firstName, lastName, phoneNumber, passwordHash);
        this.type = type;
        this.branchID = branchID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
