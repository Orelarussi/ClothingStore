package client.serverCommunication.decodeCMD;
import client.serverCommunication.Format;
import models.customer.Customer;
import server.services.CustomerManager;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandCustomer {
    public static String execute(String command) throws SQLException {
        CustomerManager DAO = new CustomerManager();
        Customer customer;
        List<Customer> customers;
        String response = Format.encodeSuccessMessage();
        String firstParam = Format.getFirstParam(command);
        switch (Format.getMethod(command)) {
            case "createNewCustomer":
                // static String createNewCustomer(Customer customer, String customerType) {
                customer = Customer.deserializeFromString(firstParam);
                DAO.addCustomer(customer);
                break;
            case "getCustomerByID":
                // public Customer getCustomerByID(int id) {
                int id = Integer.parseInt(firstParam);
                customer = DAO.findCustomerById(id);
                if (customer != null)
                    response = customer.serializeToString();
                else
                    response = Format.encodeEmpty("No customer with this ID was found in the system.");
                break;
            case "updateCustomer":
                // public void updateCustomer(Customer customer, String customerType) {
                customer = Customer.deserializeFromString(firstParam);
                DAO.updateCustomer(customer.getId(),customer.getPhoneNumber(), customer.getType(),customer.getBranchID() );
                break;
            case "deleteCustomer":
                // public void deleteCustomer(int id) {
                DAO.deleteCustomer(Integer.parseInt(firstParam));
                break;
            case "getCustomers":
                //     public List<Customer> getCustomers() {
                customers = DAO.getAllCustomers();
                if (customers.isEmpty())
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeCustomers(customers);
                break;
        }
        return response;

    }
}