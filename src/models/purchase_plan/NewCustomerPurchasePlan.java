package models.purchase_plan;

public class NewCustomerPurchasePlan extends PurchasePlan {

    public NewCustomerPurchasePlan() {
        this.planDetails = "New customer: 10% off on first purchase.";
    }

    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, NewCustomerPurchasePlan.class);
        this.customerID = plan.customerID;
        this.purchaseID = plan.purchaseID;
        this.planDetails = plan.planDetails;
    }
}
