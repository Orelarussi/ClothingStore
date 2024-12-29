package models.purchaseHistory;


import com.google.gson.Gson;
import utils.JsonSerializable;

/**
 * Represents an item that has been purchased, detailing the purchase ID and the product ID.
 */
public class PurchasedItem implements JsonSerializable {

    private int purchaseID;
    private int productID;

    /**
     * Creates a new purchased item instance with the given purchase ID and product ID.
     *
     * @param purchaseID Unique identifier for the purchase.
     * @param productID Identifier of the purchased product.
     */
    public PurchasedItem(int purchaseID, int productID) {
        this.purchaseID = purchaseID;
        this.productID = productID;
    }

    public PurchasedItem(String objectString) {
        this.deserializeFromString(PurchasedItem.class,objectString);
    }

    /**
     * Retrieves the purchase ID associated with the purchased item.
     *
     * @return The unique identifier for the purchase.
     */
    public int getPurchaseID() {
        return purchaseID;
    }

    /**
     * Sets the purchase ID for this purchased item.
     *
     * @param purchaseID The unique identifier to be set for the purchase.
     */
    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    /**
     * Retrieves the product ID of the purchased item.
     *
     * @return The identifier of the purchased product.
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Sets the product ID for this purchased item.
     *
     * @param productID The identifier to be set for the purchased product.
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "PurchasedItem{" +
                "purchaseID=" + purchaseID +
                ", productID=" + productID +
                '}';
    }

    /**
        * Serializes the purchased item object to a string representation.
        *
        * @return The serialized string representation of the purchased item.
        */
    public String serializeToString() {
        return new Gson().toJson(this);
    }

    /**
        * Deserializes a string representation of an purchased item back to an PurchasedItem object.
        *
        * @param serializedString The serialized string representation of the purchased item.
        * @return The deserialized PurchasedItem object.
        */
//    public static PurchasedItem deserializeFromString(String serializedString) {
//        return new Gson().fromJson(serializedString, PurchasedItem.class);
//    }
}


/*
CREATE TABLE PurchaseHistoryItems (
	purchaseID int NOT NULL,
	productID int NOT NULL
);
 */
