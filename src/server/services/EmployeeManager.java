package server.services;

import com.google.gson.JsonObject;
import server.logger.Logger;
import server.models.Employee;
import server.models.customer.Customer;
import server.utils.JsonUtils;

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
    public boolean deleteCustomer(int id) {
        if (customers.containsKey(id)) {
            customers.remove(id);
            System.out.println("Customer with ID " + id + " removed.");
            Logger.log("Customer with ID " + id + " removed.", Logger.LogType.CUSTOMER);

            JsonUtils.saveCustomers();
            return true;
        }
        System.out.println("Customer with ID " + id + " not found.");
        return false;
    }

    public boolean addCustomer(Customer customer) {
        if (!customers.containsKey(customer.getId())) {
            customers.put(customer.getId(), customer);
            System.out.println("Customer added: " + customer.getFirstName() + " " + customer.getLastName());
            Logger.log("Customer added: " + customer.getFirstName() + " " + customer.getLastName(), Logger.LogType.CUSTOMER);
            JsonUtils.saveCustomers();
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

    // Get employees by branch ID
    public List<Employee> getEmployeesByBranchId(int branchId) {
        return AdminManager.getInstance().getAllEmployees().stream()
                .filter(employee -> employee.getBranchID() == branchId)
                .collect(Collectors.toList());
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
