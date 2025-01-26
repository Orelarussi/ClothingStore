package server.models.customer;

import com.google.gson.Gson;
import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.ReturningCustomerPurchasePlan;

public class ReturningCustomer extends Customer{
    public ReturningCustomer(int id, String first, String last, String phone, int totalPurchases) {
        super(id, first, last, phone, CustomerType.RETURNING, totalPurchases);
    }

    @Override
    protected PurchasePlan createPurchasePlan() {
        return new ReturningCustomerPurchasePlan();
    }

    @Override
    protected void populateFromJson(String json) {
        ReturningCustomer temp = new Gson().fromJson(json, ReturningCustomer.class);
    // Customer
        this.purchasePlan = temp.purchasePlan;
        this.type = temp.type;
    // Person
        this.id = temp.id;
        this.firstName = temp.firstName;
        this.lastName = temp.lastName;
        this.phoneNumber = temp.phoneNumber;
    }
}
