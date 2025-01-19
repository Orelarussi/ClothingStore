package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;
import server.models.customer.Customer;

public class CustomerHandler extends BaseHandler{

    private static CustomerHandler instance = new CustomerHandler();

    public static CustomerHandler getInstance() {
        return instance;
    }

    private CustomerHandler() {
        super(ServiceType.CUSTOMER);
    }

    public String addCustomer(Customer customer) {
        JsonObject json = customer.serializeToJson();
        return super.encodeRequest(MethodType.ADD_CUSTOMER,json);
    }
    public String deleteCustomer(int customerId) {
        JsonObject json = new JsonObject();
        json.addProperty("id", customerId);
        return super.encodeRequest(MethodType.DELETE_CUSTOMER,json);
    }

    public String getAllCustomers() {
        return super.encodeRequest(MethodType.GET_ALL_CUSTOMERS,new JsonObject());
    }
}
