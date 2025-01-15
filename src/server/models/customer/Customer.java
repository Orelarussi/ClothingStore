package server.models.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import server.models.Person;
import server.models.purchase_plan.PurchasePlan;
import server.models.purchase_plan.PurchasePlanInstanceCreator;

public abstract class Customer extends Person {

    private int branchID;
    protected PurchasePlan purchasePlan;
    protected CustomerType type;

    public Customer(int id, String firstName, String lastName, String phoneNumber, int branchID, CustomerType type) {
        super(id, firstName, lastName, phoneNumber);
        this.branchID = branchID;
        this.purchasePlan = createPurchasePlan();
        this.type = type;
    }

    public Customer(JsonObject json) {
        populateFromJson(json.toString());
    }

    /**
     * Deserializes a string representation of a customer back to a Customer object.
     *
     * @param objString The serialized string representation of the customer.
     * @return The deserialized Customer object.
     * @throws IllegalArgumentException if the class type in the string is unknown.
     */
    public static Customer deserializeFromString(String objString) {
        JsonObject json = gson.fromJson(objString, JsonObject.class);
        String type = json.get("type").getAsString();
        CustomerType customerType = CustomerType.valueOf(type);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Customer.class, new CustomerInstanceCreator(json))
                .registerTypeAdapter(PurchasePlan.class, new PurchasePlanInstanceCreator(customerType))
                .create();

        return gson.fromJson(objString, Customer.class);
    }

    protected abstract PurchasePlan createPurchasePlan();

    @Override
    protected void populateFromJson(String json) {
        super.populateFromJson(json);
        JsonObject temp = gson.fromJson(json, JsonObject.class);
        this.branchID = temp.get("branchID").getAsInt();
//        this.purchasePlan = temp.get("purchasePlan").getAsString();
        this.type = CustomerType.valueOf(temp.get("type").getAsString());
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public PurchasePlan getPurchasePlan() {
        return purchasePlan;
    }

    public void setPurchasePlan(PurchasePlan purchasePlan) {
        this.purchasePlan = purchasePlan;
    }

    @Override
    public String toString() {
        return super.toString() + "Customer{" +
                ", branchID='" + branchID + '\'' +
                ", plan='" + purchasePlan + '\'' +
                '}';
    }

    public CustomerType getType() {
        return type;
    }
}
