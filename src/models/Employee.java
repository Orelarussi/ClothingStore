package models;

import java.util.Objects;

public class Employee extends User {
    private String branchID;
    private long accountNumber;
    private long employeeNumber;
    private Position position;
    private String password;


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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Override toString for detailed employee info
    @Override
    public String toString() {
        return super.toString() + " Employee{" +
                "branchID='" + branchID + '\'' +
                ", accountNumber=" + accountNumber +
                ", employeeNumber=" + employeeNumber +
                ", position=" + position +
                '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return accountNumber == employee.accountNumber &&
                employeeNumber == employee.employeeNumber &&
                Objects.equals(branchID, employee.branchID) &&
                position == employee.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchID, accountNumber, employeeNumber, position);
    }
}
