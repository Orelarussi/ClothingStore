package server.command_executors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.models.customer.Customer;
import server.services.EmployeeManager;
import server.services.LoginResult;

import java.util.Map;

public class CustomerManagerCommandExecutor implements IExecute{

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        EmployeeManager manager = EmployeeManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();

        switch (method){
            case ADD_CUSTOMER:
                Customer customer = Customer.deserializeFromString(data.toString());
                boolean added = manager.addCustomer(customer);
                response.addProperty("success", added);
                break;
            case DELETE_CUSTOMER:
                int id = data.get("id").getAsInt();
                boolean wasDeleted = manager.deleteCustomer(id);
                response.addProperty("success", wasDeleted);
                break;
            case GET_ALL_CUSTOMERS:
                Map<Integer, Customer> customers = manager.getCustomers();
                JsonArray jsonArray = new JsonArray(customers.size());
                customers.forEach((_, value) -> {
                    jsonArray.add(value.serializeToJson());
                });
                response.add("customers", jsonArray);
                response.addProperty("success", true);
                break;
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
