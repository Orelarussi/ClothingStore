package server.models;

public abstract class User extends Person {
    protected String password;

    public User(int id, String firstName, String lastName, String phoneNumber, String password) {
        super(id, firstName, lastName, phoneNumber);
        this.password = password;
    }

    protected User() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
