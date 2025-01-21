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

    public String saleProduct(int customerId, int productId, int amount) {
        JsonObject data = new JsonObject();
        data.addProperty("customerId", customerId);
        data.addProperty("productId", productId);
        data.addProperty("amount", amount);
        return super.encodeRequest(MethodType.SALE_PRODUCT, data);
    }
}
