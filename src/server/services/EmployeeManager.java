package server.services;

import client.serverCommunication.Format;
import server.models.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.models.customer.Customer;
import server.models.customer.CustomerType;
import server.Server;

public class EmployeeManager {
    private Map<Integer, Employee> employees = new HashMap<>();
    private List<Customer> customers = new ArrayList<>();

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


    public String login(String username, String password) {

        Employee emp = getEmployeeByID(Integer.parseInt(username));
        if (emp == null) return Format.encodeException("There's no such user");

        if (!emp.getPassword().equals(password)) return Format.encodeException("wrong password");

        if (Server.getSocketDataByEmployee(emp) != null) return Format.encodeException("User already logged in");

        return emp.serializeToString();
    }

    public Employee getEmployeeByID(int id) {
        return employees.get(id);
    }
}
