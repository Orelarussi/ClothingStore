package server.services;

import server.models.customer.Customer;
import server.models.customer.CustomerType;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    // Add a new customer
    public void addCustomer(Customer customer) {
        for (Customer existingCustomer : customers) {
            if (existingCustomer.getId() == customer.getId()) {
                System.out.println("Customer with ID " + customer.getId() + " already exists.");
                return;
            }
        }
        customers.add(customer);
        System.out.println("Customer added: " + customer.getFirstName() + " " + customer.getLastName());
    }

    // Update customer details
    public void updateCustomer(int id, String phoneNumber, CustomerType type, String branchID) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                customer.setPhoneNumber(phoneNumber);
                customer.setBranchID(branchID);
                System.out.println("Customer updated: " + customer.getFirstName() + " " + customer.getLastName());
                return;
            }
        }
        System.out.println("Customer not found.");
    }

    // Delete a customer
    public void deleteCustomer(int id) {
        customers.removeIf(customer -> customer.getId() == id);
        System.out.println("Customer with ID " + id + " removed.");
    }

    // Search for a customer by ID
    public Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        System.out.println("Customer with ID " + id + " not found.");
        return null;
    }

    // Display all customers
    public void displayAllCustomers() {
        System.out.println("All Customers:");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers); // Return a copy of the customer list to avoid external modifications
    }
}
