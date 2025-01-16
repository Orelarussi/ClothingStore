package server.models.purchase_plan;

public class ReturningCustomerPurchasePlan extends PurchasePlan {

    public ReturningCustomerPurchasePlan() {
        super();
        this.discountPercent = 15;
    }


    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, ReturningCustomerPurchasePlan.class);
        this.discountPercent = plan.discountPercent;
    }
}
