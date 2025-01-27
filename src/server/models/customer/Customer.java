package server.models.customer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.models.Person;

public abstract class Customer extends Person {

    protected String purchasePlan;
    protected CustomerType type;
    private int totalPurchases;


    public Customer(int id, String firstName, String lastName, String phoneNumber, CustomerType type, int totalPurchases) {
        super(id, firstName, lastName, phoneNumber);
        this.purchasePlan = createPurchasePlan();
        this.type = type;
        this.totalPurchases = totalPurchases;
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
                return new NewCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString(), json.get("totalPurchases").getAsInt());
            }
            case "RETURNING" -> {
                return new ReturningCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString(), json.get("totalPurchases").getAsInt());
            }
            case "VIP" -> {
                return new VIPCustomer(json.get("id").getAsInt(), json.get("firstName").getAsString(), json.get("lastName").getAsString() ,json.get("phoneNumber").getAsString(), json.get("totalPurchases").getAsInt());
            }
            default -> throw new IllegalArgumentException("Unknown class type: " + type);
        }
    }

    protected abstract String createPurchasePlan();

    public String getPurchasePlan() {
        return purchasePlan;
    }

    public void setPurchasePlan(String purchasePlan) {
        this.purchasePlan = purchasePlan;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;

        if(this.type != CustomerType.VIP && this.totalPurchases > 10) {
            this.type = CustomerType.VIP;
            this.purchasePlan = PurchasePlan.VIP.getDetails();
        } else if (this.type != CustomerType.RETURNING &&  this.totalPurchases > 5) {
            this.type = CustomerType.RETURNING;
            this.purchasePlan = PurchasePlan.RETURNING.getDetails();
        }
    }

    @Override
    public String toString() {
        return "Customer \n {" +
                "id =" + getId() +", " +
                "full name =" + getFullName() +", " +
                "type =" + getType() + '}';
    }

    public CustomerType getType() {
        return type;
    }

    enum PurchasePlan{
        NEW("New customer: 10% off on first purchase."),
        RETURNING("Returning customer: 15% off after 3 purchases."),
        VIP("VIP customer: 20% off on all purchases.");

        private String details;

        PurchasePlan(String s) {
            this.details = s;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}
