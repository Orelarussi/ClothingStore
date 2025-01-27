package server.command_executors;

import com.google.gson.JsonObject;
import server.models.Branch;
import server.models.Employee;
import server.services.*;

import java.util.List;

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
                        result.append(String.format("ProductId %s  : amount %s, ", productId, value)));

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
            case SHOW_BRANCH_EMPLOYEE:
                // Get branchId from the request
                int branchEmployeeId = data.get("branchId").getAsInt();

                // Fetch employees for the given branchId
                List<Employee> employees = EmployeeManager.getInstance().getEmployeesByBranchId(branchEmployeeId);

                // Build the result string
                employees.forEach(employee ->
                        result.append(String.format("Employee -> %s, ", employee))
                );

                // If no employees found, return an appropriate message
                if (!result.isEmpty()) {
                    result.setLength(result.length() - 2); // Remove trailing ", "
                } else {
                    return "No employees found for this branch.";
                }

                return result.toString();

            case ADD_PRODUCT_TO_BRANCH:
                int productBranchId = data.get("branchId").getAsInt();
                int productToAddId = data.get("productId").getAsInt();
                int amountToAdd = data.get("amount").getAsInt();

                Branch branch = BranchManager.getInstance().getBranchById(productBranchId);

                if (ProductManager.getInstance().getProduct(productToAddId) == null) {
                    return "No found product.";
                }

                branch.getInventory().put(productToAddId, branch.getInventory().getOrDefault(productToAddId, 0) + amountToAdd);

                return "Product Added Successfully.";
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
