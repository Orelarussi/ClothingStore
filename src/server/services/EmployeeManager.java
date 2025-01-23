package server.services;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;
import server.models.customer.Customer;

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
    public boolean deleteCustomer(int id) {
        if (customers.containsKey(id)) {
            customers.remove(id);
            System.out.println("Customer with ID " + id + " removed.");
            return true;
        }
        System.out.println("Customer with ID " + id + " not found.");
        return false;
    }

    public boolean addCustomer(Customer customer) {
        if (!customers.containsKey(customer.getId())) {
            customers.put(customer.getId(), customer);
            System.out.println("Customer added: " + customer.getFirstName() + " " + customer.getLastName());
            return true;
        } else {
            System.out.println("Customer with ID " + customer.getId() + " already exists.");
            return false;
        }
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers.stream()
                .map(c -> new AbstractMap.SimpleEntry<>(c.getId(), c))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public void setCustomersFromJson(List<JsonObject> employeesJsonList) {
        List<Customer> customers = employeesJsonList.stream()
                .map(json -> {
                    String asString = json.toString();
                    Customer customer = Customer.deserializeFromString(asString);
                    return customer;
                }).toList();
        this.setCustomers(customers);
    }
}
