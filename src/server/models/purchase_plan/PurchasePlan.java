package server.models.purchase_plan;

import server.utils.JsonSerializable;

public abstract class PurchasePlan extends JsonSerializable {
    protected int discountPercent;

    public PurchasePlan(){}

    public int getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public String toString() {
        return "PurchasePlan{" +
                "discountPercent=" + discountPercent + '}';
    }
}
