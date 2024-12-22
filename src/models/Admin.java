package models;

public class Admin extends User {
    public Admin(int id, String firstName, String lastName, String phoneNumber, String passwordHash) {
        super(id, firstName, lastName, phoneNumber);
    }
}
