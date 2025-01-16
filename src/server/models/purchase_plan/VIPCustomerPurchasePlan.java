package server.models.purchase_plan;

public class VIPCustomerPurchasePlan extends PurchasePlan {

    public VIPCustomerPurchasePlan() {
        this.discountPercent = 20;
    }

    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, VIPCustomerPurchasePlan.class);
        this.discountPercent = plan.discountPercent;
    }
}
