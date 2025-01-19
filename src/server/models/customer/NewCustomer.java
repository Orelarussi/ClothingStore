package server.models.customer;

import server.models.purchase_plan.NewCustomerPurchasePlan;
import server.models.purchase_plan.PurchasePlan;

public class NewCustomer extends Customer {
    public NewCustomer(int id, String first, String last, String phone) {
        super(id, first, last, phone, CustomerType.NEW);
    }

    @Override
    public PurchasePlan createPurchasePlan() {
        return new NewCustomerPurchasePlan();
    }
}
