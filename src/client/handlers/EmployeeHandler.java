package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

public class EmployeeHandler extends BaseHandler{

    private static EmployeeHandler instance = new EmployeeHandler();

    public static EmployeeHandler getInstance() {
        return instance;
    }

    private EmployeeHandler() {
        super(ServiceType.EMPLOYEE);
    }

    public String showInventory(int branchId) {
        JsonObject data = new JsonObject();
        data.addProperty("branchId",branchId);
        return super.encodeRequest(MethodType.GET_INVENTORY_BY_BRANCH, data);
    }
}
