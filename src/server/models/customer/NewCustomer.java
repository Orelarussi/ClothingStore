package server.models.customer;

import com.google.gson.JsonObject;
import server.models.purchase_plan.NewCustomerPurchasePlan;
import server.models.purchase_plan.PurchasePlan;

public class NewCustomer extends Customer {
    public NewCustomer(int id, String first, String last, String phone, int branch) {
        super(id, first, last, phone, branch, CustomerType.NEW);
    }

    public NewCustomer(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public PurchasePlan createPurchasePlan() {
        return new NewCustomerPurchasePlan();
    }
}
