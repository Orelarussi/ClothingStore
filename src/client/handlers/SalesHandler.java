package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

import java.time.LocalDate;
import java.util.Date;

public class SalesHandler extends BaseHandler{

    private static SalesHandler instance = new SalesHandler();

    public static SalesHandler getInstance() {
        return instance;
    }

    private SalesHandler() {
        super(ServiceType.SALES);
    }

    public String showSalesByBranch(int branchId){
        JsonObject data = new JsonObject();
        data.addProperty("branchId", branchId);
        return super.encodeRequest(MethodType.SHOW_SALES_BY_BRANCH,data);
    }

    public String showSalesByProduct(int productId){
        JsonObject data = new JsonObject();
        data.addProperty("productId", productId);
        return super.encodeRequest(MethodType.SHOW_SALES_BY_PRODUCT,data);
    }

    public String showSalesByDate(LocalDate date){
        JsonObject data = new JsonObject();
        data.addProperty("date", String.valueOf(date));
        return super.encodeRequest(MethodType.SHOW_SALES_BY_DATE,data);
    }
}
