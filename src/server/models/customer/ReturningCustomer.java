package server.models.customer;

import com.google.gson.JsonObject;
import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.ReturningCustomerPurchasePlan;

public class ReturningCustomer extends Customer {
    public ReturningCustomer(int id, String first, String last, String phone, int branch) {
        super(id, first, last, phone, branch, CustomerType.RETURNING);
    }

    public ReturningCustomer(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected PurchasePlan createPurchasePlan() {
        return new ReturningCustomerPurchasePlan();
    }

}
