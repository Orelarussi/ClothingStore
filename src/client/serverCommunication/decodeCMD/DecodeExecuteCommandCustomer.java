package client.serverCommunication.decodeCMD;
import client.serverCommunication.Format;
import models.customer.Customer;
import models.customer.CustomerType;
import services.CustomerManager;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandCustomer {
    public static String execute(String command) throws SQLException {
        CustomerManager DAO = new CustomerManager();
        Customer customer;
        List<Customer> customers;
        String response = Format.encodeSuccessMessage();
        switch (Format.getMethod(command)) {
            case "createNewCustomer":
                // static String createNewCustomer(Customer customer, String customerType) {
                customer = Customer.deserializeFromString(Format.getFirstParam(command));
                String customerType = Format.getSecondParam(command);
                DAO.addCustomer(customer);
                break;
            case "getCustomerByID":
                // public Customer getCustomerByID(int id) {
                int id = Integer.parseInt(Format.getFirstParam(command));
                customer = DAO.findCustomerById(id);
                if (customer != null)
                    response = customer.serializeToString();
                else
                    response = Format.encodeEmpty("לא נמצא לקוח עם התעודת זהות הזאת במערכת");
                break;
            case "updateCustomer":
                // public void updateCustomer(Customer customer, String customerType) {
                customer = Customer.deserializeFromString(Format.getFirstParam(command));
                DAO.updateCustomer(customer.getId(),customer.getPhoneNumber(), customer.getType(),customer.getBranchID() );
                break;
            case "deleteCustomer":
                // public void deleteCustomer(int id) {
                DAO.deleteCustomer(Integer.parseInt(Format.getFirstParam(command)));
                break;
            case "getCustomers":
                //     public List<Customer> getCustomers() {
                customers = DAO.getAllCustomers();
                if (customers.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeCustomers(customers);
                break;
        }
        return response;

    }
}