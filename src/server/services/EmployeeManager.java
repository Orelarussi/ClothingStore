package server.services;

import server.models.customer.Customer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeManager {
    private Map<Integer, Customer> customers = new HashMap<>();
    private static EmployeeManager instance;

    public static synchronized EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }

    // Delete a customer
    public void deleteCustomer(int id) {
        if (customers.containsKey(id)) {
            customers.remove(id);
            System.out.println("Customer with ID " + id + " removed.");
        } else {
            System.out.println("Customer with ID " + id + " not found.");
        }
    }

    public void addCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            System.out.println("Customer with ID " + customer.getId() + " already exists.");
            return;
        }
        customers.put(customer.getId(), customer);
        System.out.println("Customer added: " + customer.getFirstName() + " " + customer.getLastName());
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers.stream()
                .map(c -> new AbstractMap.SimpleEntry<>(c.getId(), c))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
