package client.serverCommunication.encodeCMD;


import client.serverCommunication.ClassType;
import client.serverCommunication.Format;
import models.purchase_plan.PurchasePlan;

public class EncodeCommandPurchaseHistory {
    public static String createNewPurchase(PurchasePlan purchase) {
        return Format.encode(ClassType.PURCHASE_HISTORY,
                "createNewPurchase",
                purchase.serializeToString());
    }

    public static String getItemsFromOrdersByBranchAndDays(String branch, int days) {
        return Format.encode(ClassType.PURCHASE_HISTORY, "getItemsFromOrdersByBranchAndDays", branch, Integer.toString(days));
    }
}
