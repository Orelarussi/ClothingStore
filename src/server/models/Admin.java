package server.models;

public class Admin extends User {

    public Admin(int id, String firstName, String lastName, String phoneNumber, String password) {
        super(id, firstName, lastName, phoneNumber, password);
    }

    @Override
    protected void populateFromJson(String json) {
        Admin temp = gson.fromJson(json,Admin.class);

        //Person
        this.id = temp.id;
        this.firstName = temp.firstName;
        this.lastName = temp.lastName;
        this.phoneNumber = temp.phoneNumber;
        //User
        this.password = temp.password;
        

    }
}
