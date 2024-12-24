package models.customer;

import client.serverCommunication.Format;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.User;
import models.purchase_plan.PurchasePlan;

import java.util.Map;

public abstract class Customer extends User {

    private String branchID;
    protected PurchasePlan purchasePlan;
    protected final CustomerType type;

    public Customer(int id, String firstName, String lastName, String phoneNumber, String branchID, CustomerType type) {
        super(id, firstName, lastName, phoneNumber);
        this.branchID = branchID;
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
                return gson.fromJson(objString, NewCustomer.class);
            }
            case "RETURNING" -> {
                return gson.fromJson(objString, ReturningCustomer.class);
            }
            case "VIP" -> {
                return gson.fromJson(objString, VIPCustomer.class);
            }
            default -> throw new IllegalArgumentException("Unknown class type: " + type);
        }
    }

    protected abstract PurchasePlan createPurchasePlan();

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
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
                ", plan='" + purchasePlan +'\'' +
                '}';
    }

    public abstract String serializeToString();
}
