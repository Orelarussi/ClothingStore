package models.customer;

import com.google.gson.Gson;
import models.purchase_plan.NewCustomerPurchasePlan;
import models.purchase_plan.PurchasePlan;

public class NewCustomer extends Customer {
    public NewCustomer(int id, String first, String last, String phone, String branch, String pass) {
        super(id, first, last, phone, branch, CustomerType.NEW);
    }

    @Override
    public String serializeToString() {
        return new Gson().toJson(this);
    }

    @Override
    public <T> T deserializeFromString(Class<T> type, String objectString) {
        return new Gson().fromJson(objectString, type);
    }

    @Override
    public PurchasePlan createPurchasePlan() {
        return new NewCustomerPurchasePlan();
    }
}
