package models.customer;

import models.purchase_plan.PurchasePlan;
import models.purchase_plan.ReturningCustomerPurchasePlan;

public class ReturningCustomer extends Customer{
    public ReturningCustomer(int id, String first, String last, String phone, String branch, String pass) {
        super(id, first, last, phone, branch);
    }
    @Override
    protected PurchasePlan createPurchasePlan() {
        return new ReturningCustomerPurchasePlan();
    }
}
