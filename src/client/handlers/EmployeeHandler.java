package client.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;
import server.models.Product;

public class EmployeeHandler extends BaseHandler {

    private static EmployeeHandler instance = new EmployeeHandler();

    public static EmployeeHandler getInstance() {
        return instance;
    }

    private EmployeeHandler() {
        super(ServiceType.EMPLOYEE);
    }

    public String showBranchEmployee(int branchId){
        JsonObject data = new JsonObject();
        data.addProperty("branchId",branchId);
        return super.encodeRequest(MethodType.SHOW_BRANCH_EMPLOYEE, data);
    }

    public String addProductToBranch(int branchId, int productId, int amount){
        JsonObject data = new JsonObject();
        data.addProperty("productId",productId);
        data.addProperty("branchId",branchId);
        data.addProperty("amount",amount);
        return super.encodeRequest(MethodType.ADD_PRODUCT_TO_BRANCH, data);
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
