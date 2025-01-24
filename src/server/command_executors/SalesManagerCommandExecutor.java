package server.command_executors;

import com.google.gson.JsonObject;
import server.services.LoginResult;
import server.services.SalesManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesManagerCommandExecutor implements IExecute{

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        SalesManager salesManager = SalesManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        StringBuilder result = new StringBuilder();

        switch (method){
            case SHOW_SALES_BY_BRANCH:
                int branchId = data.get("branchId").getAsInt();

                salesManager.getAllSaleReports().stream()
                        .filter(saleReport -> saleReport.getBranchID() == branchId)
                        .forEach(saleReport -> result.append(String.format("sale -> %s, ", saleReport)));

                if (result.isEmpty()) {
                    return "Not Found Sales";
                }

                result.setLength(result.length() - 2);
                return result.toString();
            case SHOW_SALES_BY_PRODUCT:
                int productId = data.get("productId").getAsInt();

                salesManager.getAllSaleReports().stream()
                        .filter(saleReport -> saleReport.getProductId() == productId)
                        .forEach(saleReport -> result.append(String.format("sale -> %s , ", saleReport)));

                if (result.isEmpty()) {
                    return "Not Found Sales";
                }

                result.setLength(result.length() - 2);
                return result.toString();
            case SHOW_SALES_BY_DATE:
                LocalDate date = LocalDate.parse(data.get("date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                salesManager.getAllSaleReports().stream()
                        .filter(saleReport -> saleReport.getDate().isEqual(date))
                        .forEach(saleReport -> result.append(String.format("sale -> %s , ", saleReport)));

                if (result.isEmpty()) {
                    return "Not Found Sales";
                }

                result.setLength(result.length() - 2);
                return result.toString();
            default:
                response.addProperty("success", false);
                break;
        }

        return response.toString();
    }
}
