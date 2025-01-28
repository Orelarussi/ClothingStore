package server.command_executors.executors;

import com.google.gson.JsonObject;
import server.command_executors.IExecute;
import server.command_executors.MethodType;
import server.command_executors.ServerDecoder;
import server.models.Employee;
import server.services.*;

import java.util.List;

public class EmployeeManagerCommandExecutor implements IExecute {

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

                branchManager.getBranchById(branchId).getInventory()
                        .forEach((productId, value) -> {
                            String formatted = String.format("ProductId %s  : amount %s", productId, value);
                            result.append(formatted).append(System.lineSeparator());
                        });
                response.addProperty("result", result.toString());
                return response.toString();

            case SALE_PRODUCT:
                int customerId = data.get("customerId").getAsInt();
                int productId = data.get("productId").getAsInt();
                int amount = data.get("amount").getAsInt();

                return salesManager.addProductSale(AdminManager.getInstance().findEmployeeById(AdminManager.currentUserId).getBranchID(),
                        customerId, productId, amount);
            case SHOW_BRANCH_EMPLOYEE:
                // Get branchId from the request
                int branchEmployeeId = data.get("branchId").getAsInt();

                // Fetch employees for the given branchId
                List<Employee> employees = EmployeeManager.getInstance().getEmployeesByBranchId(branchEmployeeId);

                // Build the result string
                employees.forEach(employee -> result.append(employee).append("\n"));
                response.addProperty("result", result.toString());
                return response.toString();

            case ADD_PRODUCT_TO_BRANCH:
                int productBranchId = data.get("branchId").getAsInt();
                int productToAddId = data.get("productId").getAsInt();
                int amountToAdd = data.get("amount").getAsInt();

                BranchManager.getInstance().addProductToBranch(productBranchId,productToAddId,amountToAdd);

                if (ProductManager.getInstance().getProduct(productToAddId) == null) {
                    return "No found product.";
                }

                return "Product Added Successfully.";
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
