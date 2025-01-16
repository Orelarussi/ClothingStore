package server.models.purchase_plan;

public class NewCustomerPurchasePlan extends PurchasePlan {

    public NewCustomerPurchasePlan() {
        this.discountPercent = 10;
    }

    @Override
    protected void populateFromJson(String json) {
        PurchasePlan plan = gson.fromJson(json, NewCustomerPurchasePlan.class);
        this.discountPercent = plan.discountPercent;
    }
}
