package server.models.customer;

public class NewCustomer extends Customer {
    public NewCustomer(int id, String first, String last, String phone, int totalPurchases) {
        super(id, first, last, phone, CustomerType.NEW, totalPurchases);
    }

    @Override
    public String createPurchasePlan() {
        return PurchasePlan.NEW.getDetails();
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
