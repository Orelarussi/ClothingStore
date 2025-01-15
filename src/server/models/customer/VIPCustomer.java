package server.models.customer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.VIPCustomerPurchasePlan;

public class VIPCustomer extends Customer{
    public VIPCustomer(int id, String first, String last, String phone, int branch) {
        super(id, first, last, phone, branch,CustomerType.VIP);
    }

    public VIPCustomer(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected PurchasePlan createPurchasePlan() {
        return new VIPCustomerPurchasePlan();
    }

    @Override
    public String serializeToString() {
        return new Gson().toJson(this);
    }
}
