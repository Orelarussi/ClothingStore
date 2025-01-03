package models.purchase_plan;

public class VIPCustomerPurchasePlan extends PurchasePlan {

    public VIPCustomerPurchasePlan() {
        this.planDetails = "VIP customer: 20% off on all purchases.";
    }

    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, VIPCustomerPurchasePlan.class);
        this.customerID = plan.customerID;
        this.purchaseID = plan.purchaseID;
        this.planDetails = plan.planDetails;
    }
}
