package server.services;

import server.models.Person;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private Set<Person> loggedInPeople;

    public SessionManager() {
        this.loggedInPeople = new HashSet<>();
    }

    // Check if user is already logged in
    public boolean isUserLoggedIn(Person person) {
        return loggedInPeople.contains(person);
    }

    // Log in a user
    public boolean logInUser(Person person) {
        if (isUserLoggedIn(person)) {
            System.out.println("User " + person.getFirstName() + " is already logged in.");
            return false;
        } else {
            loggedInPeople.add(person);
            System.out.println("User " + person.getFirstName() + " logged in successfully.");
            return true;
        }
    }

    // Log out a user
    public void logOutUser(Person person) {
        if (loggedInPeople.remove(person)) {
            System.out.println("User " + person.getFirstName() + " logged out successfully.");
        } else {
            System.out.println("User " + person.getFirstName() + " is not logged in.");
        }
    }
}
