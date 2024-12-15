package models.customer;

import models.purchase_plan.NewCustomerPurchasePlan;
import models.purchase_plan.PurchasePlan;

public class NewCustomer extends Customer {
    public NewCustomer(int id, String first, String last, String phone, String branch, String pass) {
        super(id, first, last, phone, branch, pass);
    }
    @Override
    public PurchasePlan createPurchasePlan() {
        return new NewCustomerPurchasePlan();
    }
}
