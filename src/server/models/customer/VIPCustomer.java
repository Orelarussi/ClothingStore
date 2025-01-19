package server.models.customer;

import com.google.gson.Gson;
import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.VIPCustomerPurchasePlan;

public class VIPCustomer extends Customer{
    public VIPCustomer(int id, String first, String last, String phone) {
        super(id, first, last, phone, CustomerType.VIP);
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
