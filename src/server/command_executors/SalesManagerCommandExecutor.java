package server.command_executors;

import com.google.gson.JsonObject;
import server.models.SaleReport;
import server.services.LoginResult;
import server.services.SalesManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

public class SalesManagerCommandExecutor implements IExecute {

    @Override
    public String execute(Integer userId, LoginResult loginResult, String request) {
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        Predicate<SaleReport> filter = switch (method) {
            case SHOW_SALES_BY_BRANCH -> {
                int branchId = data.get("branchId").getAsInt();
                yield report -> report.getBranchID() == branchId;
            }
            case SHOW_SALES_BY_PRODUCT -> {
                int productId = data.get("productId").getAsInt();
                yield saleReport -> saleReport.getProductId() == productId;
            }
            case SHOW_SALES_BY_DATE -> {
                LocalDate date = LocalDate.parse(data.get("date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                yield saleReport -> saleReport.getDate().isEqual(date);
            }
            default -> _ -> false;
        };

        StringBuilder result = new StringBuilder();
        SalesManager.getInstance().getAllSaleReports().stream().filter(filter)
                .forEach(report -> result.append(report).append("\n"));

        JsonObject response = new JsonObject();
        response.addProperty("sales", result.toString());
        return response.toString();
    }
}
