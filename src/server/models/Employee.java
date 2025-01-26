package server.models;

public class Employee extends User {
    private static long employeesNum = 0;
    private int branchID;
    private long accountNumber;
    private long employeeNumber;
    private Position position;

    public Employee(){
        super();
    }

    public Employee(String json){
        this();
        employeesNum++; //TODO check if needed?
        populateFromJson(json);
    }

    public enum Position {SHIFTMGR, CASHIER, SELLER}

    public Employee(int id, int branchID,String firstName, String lastName, String phoneNumber, String password,
                     long accountNumber, Position position) {
        super(id, firstName, lastName, phoneNumber,password);
        this.branchID = branchID;
        this.accountNumber = accountNumber;
        this.employeeNumber = ++employeesNum;
        this.position = position;
    }

    public int getBranchID() {
        return branchID;
    }

    // Getters and Setters
    @Override
    protected void populateFromJson(String json) {
        Employee temp = gson.fromJson(json,Employee.class);

    //Person
        this.id = id;
        this.firstName = temp.firstName;
        this.lastName = temp.lastName;
        this.phoneNumber = temp.phoneNumber;
    //User
        this.password = temp.password;
     //employee
        this.branchID = temp.branchID;
        this.accountNumber = temp.accountNumber;
        this.employeeNumber = temp.employeeNumber;
        this.position = temp.position;
    }

    public void setBranchID(int branchID) {
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee with " +
                "id = " + getId() + ", " +
                "full name = " + getFullName() + ", " +
                "phoneNumber = " + getPhoneNumber() + ", " +
                "position = " + getPosition();
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