package models.purchase_plan;

public abstract class PurchasePlan {
    protected String planDetails;
    protected int customerID;
    private int purchaseID;


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
