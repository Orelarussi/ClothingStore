package server.models;

import client.serverCommunication.Format;
import server.utils.JsonSerializable;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a purchase made by a customer, detailing the items bought,
 * the date of purchase, the branch where the purchase was made, and more.
 */
public class Purchase extends JsonSerializable {

    private int purchaseID;
    private int customerID;
    private LocalDateTime date;
    private String branch;

    private List<Product> purchasedItems;



    /**
     * Creates a new purchase instance with the given customer ID, date, branch, and purchased items.
     *
     * @param customerID The ID of the customer.
     * @param date Date and time of the purchase.
     * @param branch Branch where the purchase was made.
     * @param purchasedItems List of items that were purchased.
     */
    //New purchase
    public Purchase(int customerID, LocalDateTime date, String branch, List<Product> purchasedItems) {
        this.customerID = customerID;
        this.date = date;
        this.branch = branch;
        this.purchasedItems = purchasedItems;
    }

    /**
     * Creates a new purchase instance with the given purchase ID, customer ID, date, and branch.
     *
     * @param purchaseID Unique identifier for the purchase.
     * @param customerID The ID of the customer.
     * @param date Date and time of the purchase.
     * @param branch Branch where the purchase was made.
     */
    //Creating a purchase object from the server.database
    public Purchase(int purchaseID, int customerID, LocalDateTime date, String branch) {
        this.purchaseID = purchaseID;
        this.customerID = customerID;
        this.date = date;
        this.branch = branch;
    }

    public Purchase(String jsonStr) {
        populateFromJson(jsonStr);
    }

    @Override
    protected void populateFromJson(String json) {
        Purchase tmp = gson.fromJson(json,Purchase.class);
        this.purchaseID = tmp.purchaseID;
        this.customerID = tmp.customerID;
        this.date = tmp.date;
        this.branch = tmp.branch;
    }

    /**
     * Retrieves the purchase ID.
     *
     * @return The unique identifier for the purchase.
     */
    public int getPurchaseID() {
        return this.purchaseID;
    }

    /**
     * Retrieves the customer ID associated with the purchase.
     *
     * @return The ID of the customer.
     */
    public int getCustomerID() {
        return this.customerID;
    }

    /**
     * Retrieves the date and time of the purchase.
     *
     * @return The LocalDateTime of the purchase.
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Retrieves the branch where the purchase was made.
     *
     * @return The name of the branch.
     */
    public String getBranch() {
        return this.branch;
    }

    /**
     * Sets the list of purchased items for this purchase.
     *
     * @param items The list of items to be set.
     */
    public void setPurchasedItems(ArrayList<Product> items) {
        this.purchasedItems = items;
    }

    /**
     * Retrieves the list of items included in the purchase.
     *
     * @return The list of purchased items.
     */
    public List<Product> getItems() {
        return this.purchasedItems;
    }

    /**
    * Returns a string representation of the purchase.
    *
    * @return The string representation of the purchase.
    */
    @Override
    public String toString() {
        return  purchaseID + Format.fieldSeparator +
                customerID + Format.fieldSeparator +
                date + Format.fieldSeparator +
                branch;
    }

    // Type 1: createNewPurchase, Type 2: Import Purchases List<> for Reports
    public String toString(int type) {
        String response = customerID + Format.fieldSeparator + Format.dateToString(date) + Format.fieldSeparator + branch;

        return switch (type) {
            case 1 ->
                    response + Format.paramsSeparator + Format.encodeProducts(purchasedItems) + Format.paramsSeparator;
            case 2 -> purchaseID + Format.paramsSeparator + response + Format.paramsSeparator;
            default -> null;
        };
    }

}
