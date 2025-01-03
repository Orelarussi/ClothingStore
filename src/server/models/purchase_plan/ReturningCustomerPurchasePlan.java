package server.models.purchase_plan;

public class ReturningCustomerPurchasePlan extends PurchasePlan {

    public ReturningCustomerPurchasePlan() {
        super();
        this.planDetails = "Returning customer: 15% off after 3 purchases.";
    }


    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, ReturningCustomerPurchasePlan.class);
        this.customerID = plan.customerID;
        this.purchaseID = plan.purchaseID;
        this.planDetails = plan.planDetails;
    }
}
