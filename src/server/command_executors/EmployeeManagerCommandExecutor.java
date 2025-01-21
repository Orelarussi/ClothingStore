package server.command_executors;

import com.google.gson.JsonObject;
import server.models.SaleReport;
import server.services.*;

public class EmployeeManagerCommandExecutor implements IExecute{

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        BranchManager branchManager = BranchManager.getInstance();
        SalesManager salesManager = SalesManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        StringBuilder result = new StringBuilder();

        switch (method){
            case GET_INVENTORY_BY_BRANCH:
                int branchId = data.get("branchId").getAsInt();

                branchManager.getBranchById(branchId).getInventory().forEach((productId, value) ->
                        result.append(String.format("ProductId %s : amount %s, ", productId, value)));

                if (!result.isEmpty()) {
                    result.setLength(result.length() - 2);
                }

                return result.toString();

            case SALE_PRODUCT:
                int customerId = data.get("customerId").getAsInt();
                int productId = data.get("productId").getAsInt();
                int amount = data.get("amount").getAsInt();

                return salesManager.addProductSale(AdminManager.getInstance().findEmployeeById(AdminManager.currentUserId).getBranchID(),
                        customerId, productId, amount);
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
