package models;

public class Employee extends User {
    private String branchID;
    private long accountNumber;
    private long employeeNumber;
    private Position position;
    private String password;

    public Employee(String json){
        super(-1,null,null,null);
        final Employee temp = deserializeFromString(Employee.class,json);
        this.id = temp.id;
        this.firstName = temp.firstName;
        this.lastName = temp.lastName;
        this.phoneNumber = temp.phoneNumber;
        this.branchID = temp.branchID;
        this.accountNumber = temp.accountNumber;
        this.employeeNumber = temp.employeeNumber;
        this.position = temp.position;
        this.password = temp.password;
    }

    public enum Position {SHIFTMGR, CASHIER, SELLER;}

    public Employee(int id, String firstName, String lastName, String phoneNumber, String password,
                    String branchID, long accountNumber, long employeeNumber, Position position) {
        super(id, firstName, lastName, phoneNumber);
        this.branchID = branchID;
        this.accountNumber = accountNumber;
        this.employeeNumber = employeeNumber;
        this.position = position;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters and Setters
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
        if (accountNumber > 0) {
            this.accountNumber = accountNumber;
        } else {
            throw new IllegalArgumentException("Account number must be positive.");
        }
    }

    public long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(long employeeNumber) {
        if (employeeNumber > 0) {
            this.employeeNumber = employeeNumber;
        } else {
            throw new IllegalArgumentException("Employee number must be positive.");
        }
    }

}
