package server.models.customer;

import com.google.gson.Gson;

public class VIPCustomer extends Customer{
    public VIPCustomer(int id, String first, String last, String phone, int totalPurchases) {
        super(id, first, last, phone, CustomerType.VIP, totalPurchases);
    }
    @Override
    protected String createPurchasePlan() {
        return PurchasePlan.VIP.getDetails();
    }

    @Override
    public String serializeToString() {
        return new Gson().toJson(this);
    }

    @Override
    protected void populateFromJson(String json) {
        VIPCustomer temp = new Gson().fromJson(json, VIPCustomer.class);
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
