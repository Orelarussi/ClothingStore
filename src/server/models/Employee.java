package server.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User {
    private static long employeesNum = 0;
    private int branchID;
    private long accountNumber;
    private long employeeNumber;
    private Position position;

    public Employee() {
        super();
    }

    public Employee(String json) {
        this();
        employeesNum++; //TODO check if needed?
        populateFromJson(json);
    }

    public enum Position {SHIFT_MANAGER, CASHIER, SELLER;}

    public Employee(int id, int branchID, String firstName, String lastName, String phoneNumber, String password,
                    long accountNumber, Position position) {
        super(id, firstName, lastName, phoneNumber, password);
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
        Employee temp = gson.fromJson(json, Employee.class);

        //Person
        this.id = temp.id;
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

    public static List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>(List.of(Employee.class.getDeclaredFields()));
        fields.removeIf(f -> f.getName().equals("id"));//should stay the same
        fields.removeIf(f -> f.getName().equals("employeeNumber"));//should stay the same
        fields.removeIf(f -> f.getName().equals("employeesNum"));//static

        Employee employee = new Employee();
        Class<?> superclass = employee.getClass().getSuperclass();
        while (superclass != null) {
            for (Field field : superclass.getDeclaredFields()) {
                String name = field.getName();
                if (!name.equals("gson") && !name.equals("id")) {
                    fields.add(field);
                }
            }
            superclass = superclass.getSuperclass();
        }
        return fields;
    }

    @Override
    public String toString() {
        return "Employee{" +
                super.toString() +
                "branchID=" + branchID +
                ", accountNumber=" + accountNumber +
                ", employeeNumber=" + employeeNumber +
                ", position=" + position +
                '}';
    }
}