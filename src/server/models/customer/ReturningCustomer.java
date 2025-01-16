package server.models.customer;

import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.ReturningCustomerPurchasePlan;

public class ReturningCustomer extends Customer{
    public ReturningCustomer(int id, String first, String last, String phone, String branch) {
        super(id, first, last, phone, branch,CustomerType.RETURNING);
    }

    @Override
    protected PurchasePlan createPurchasePlan() {
        return new ReturningCustomerPurchasePlan();
    }

}
