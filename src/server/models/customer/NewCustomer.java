package server.models.customer;

import server.models.Employee;
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

    @Override
    protected void populateFromJson(String json) {
        NewCustomer temp = gson.fromJson(json,NewCustomer.class);
//        Customer
        this.purchasePlan = temp.purchasePlan;
        this.type = temp.type;

//        Person
        this.id = temp.id;
        this.firstName = temp.firstName;
        this.lastName = temp.lastName;
        this.phoneNumber = temp.phoneNumber;
    }
}
