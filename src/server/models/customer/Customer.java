package server.models.customer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.models.Person;
import server.models.purchase_plan.PurchasePlan;

public abstract class Customer extends Person {

    protected PurchasePlan purchasePlan;
    protected CustomerType type;

    public Customer(int id, String firstName, String lastName, String phoneNumber, CustomerType type) {
        super(id, firstName, lastName, phoneNumber);
        this.purchasePlan = createPurchasePlan();
        this.type = type;
    }

    /**
     * Deserializes a string representation of a customer back to a Customer object.
     *
     * @param objString  The serialized string representation of the customer.
     * @return The deserialized Customer object.
     * @throws IllegalArgumentException if the class type in the string is unknown.
     */
    public static Customer deserializeFromString(String objString) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(objString, JsonObject.class);
        String type = json.get("type").getAsString();
        //According to CustomerType enum
        switch (type) {
            case "NEW" -> {
                return new NewCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString());
            }
            case "RETURNING" -> {
                return new ReturningCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString());
            }
            case "VIP" -> {
                return new VIPCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString());
            }
            default -> throw new IllegalArgumentException("Unknown class type: " + type);
        }
    }

    protected abstract PurchasePlan createPurchasePlan();

    public PurchasePlan getPurchasePlan() {
        return purchasePlan;
    }

    public void setPurchasePlan(PurchasePlan purchasePlan) {
        this.purchasePlan = purchasePlan;
    }

    @Override
    public String toString() {
        return super.toString() + "Customer{" +
                ", plan='" + purchasePlan +'\'' +
                '}';
    }

    public CustomerType getType() {
        return type;
    }
}
