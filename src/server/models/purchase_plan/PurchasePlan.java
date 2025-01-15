package server.models.purchase_plan;

import server.utils.JsonSerializable;

public abstract class PurchasePlan extends JsonSerializable {
    protected String planDetails;
    protected int customerID;
    protected int purchaseID;

    public PurchasePlan(){}

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }
}
