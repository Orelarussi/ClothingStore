package models;

public class Employee extends User {
    private String branchID;
    private long accountNumber;
    private long employeeNumber;
    private Position position;
    public enum Position {SHIFTMGR, CASHIER, SELLER;}

    public Employee(int id, String firstName, String lastName, String phoneNumber, String passwordHash,
                    String branchID, long accountNumber, long employeeNumber, Position position) {
        super(id, firstName, lastName, phoneNumber, passwordHash);
        this.branchID = branchID;
        this.accountNumber = accountNumber;
        this.employeeNumber = employeeNumber;
        this.position = position;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return super.toString() + "Employee{" +
                "branchID='" + branchID + '\'' +
                ", accountNumber=" + accountNumber +
                ", employeeNumber=" + employeeNumber +
                ", position=" + position +
                '}';

    }
}
