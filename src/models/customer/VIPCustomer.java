package models.customer;

import models.purchase_plan.PurchasePlan;
import models.purchase_plan.VIPCustomerPurchasePlan;

public class VIPCustomer extends Customer{
    public VIPCustomer(int id, String first, String last, String phone, String branch, String pass) {
        super(id, first, last, phone, branch, pass);
    }
    @Override
    protected PurchasePlan createPurchasePlan() {
        return new VIPCustomerPurchasePlan();
    }
}
