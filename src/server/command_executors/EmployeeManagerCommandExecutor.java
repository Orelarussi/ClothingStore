package server.command_executors;

import com.google.gson.JsonObject;
import server.services.BranchManager;
import server.services.EmployeeManager;
import server.services.LoginResult;

public class EmployeeManagerCommandExecutor implements IExecute{

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        EmployeeManager manager = EmployeeManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();

        switch (method){
            case GET_INVENTORY_BY_BRANCH:
                int branchId = data.get("branchId").getAsInt();
                StringBuilder result = new StringBuilder();

                BranchManager.getInstance().getBranchById(branchId).getInventory().forEach((productId, value) ->
                        result.append(String.format("ProductId %s : amount %s, ", productId, value)));

                if (!result.isEmpty()) {
                    result.setLength(result.length() - 2);
                }

                return result.toString();
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
