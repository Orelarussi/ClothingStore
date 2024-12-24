package client.serverCommunication.encodeCMD;

import Store.Customers.Customer;

public class EncodeCommandCustomer {
    public static String createNewCustomer(Customer customer, String customerType) {
        return Format.encode(ClassType.CUSTOMER, "createNewCustomer", customer.serializeToString(), customerType);
    }

    public static String updateCustomer(Customer customer, String customerType) {
        return Format.encode(ClassType.CUSTOMER, "deleteCustomer", customer.serializeToString(), customerType);
    }

    public static String deleteCustomer(int id) {
        return Format.encode(ClassType.CUSTOMER, "deleteCustomer", Integer.toString(id));
    }

    public static String getCustomerByID(int id) {
        return Format.encode(ClassType.CUSTOMER, "getCustomerByID", Integer.toString(id));
    }

    public static String getCustomersByType(String type) {
        return Format.encode(ClassType.CUSTOMER, "deleteCustomer", type);
    }

    public static String getCustomers() {
        return Format.encode(ClassType.CUSTOMER, "getCustomers");
    }
}
