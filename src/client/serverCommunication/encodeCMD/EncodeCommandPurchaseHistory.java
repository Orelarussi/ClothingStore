package client.serverCommunication.encodeCMD;

import Store.PurchaseHistory.Purchase;


public class EncodeCommandPurchaseHistory {
    public static String createNewPurchase(Purchase purchase) {
        return Format.encode(ClassType.PURCHASE_HISTORY, "createNewPurchase", purchase.serializeToString(1));
    }

    public static String getItemsFromOrdersByBranchAndDays(String branch, int days) {
        return Format.encode(ClassType.PURCHASE_HISTORY, "getItemsFromOrdersByBranchAndDays", branch, Integer.toString(days));
    }
}
