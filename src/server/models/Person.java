package server.models;

import com.google.gson.JsonObject;
import server.utils.JsonSerializable;

public abstract class Person extends JsonSerializable {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;

    public Person(int id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    protected Person() {

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    protected void populateFromJson(String json) {
        JsonObject temp = gson.fromJson(json,JsonObject.class);
        this.id = temp.get("id").getAsInt();
        this.firstName = temp.get("firstName").getAsString();
        this.lastName = temp.get("lastName").getAsString();
        this.phoneNumber = temp.get("phoneNumber").getAsString();
    }
}
