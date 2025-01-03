package models.customer;

import com.google.gson.Gson;
import models.purchase_plan.PurchasePlan;
import models.purchase_plan.VIPCustomerPurchasePlan;

public class VIPCustomer extends Customer{
    public VIPCustomer(int id, String first, String last, String phone, String branch) {
        super(id, first, last, phone, branch,CustomerType.VIP);
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
