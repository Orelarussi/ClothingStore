package client.serverCommunication.encodeCMD;


import client.serverCommunication.ClassType;
import client.serverCommunication.Format;
import server.models.Product;

public class EncodeCommandInventory {
    public static String getProductsByBranch(String branch) {
        return Format.encode(ClassType.INVENTORY, "getProductsByBranch", branch);
    }

    public static String createNewItem(Product item) {
        return Format.encode(ClassType.INVENTORY, "createNewItem", item.serializeToString());
    }

    public static String updateItem(Product item) {
        return Format.encode(ClassType.INVENTORY, "updateItem", item.serializeToString());
    }

    public static String deleteItem(int productID) {
        return Format.encode(ClassType.INVENTORY, "deleteItem", Integer.toString(productID));
    }

    public static String getItemByProductID(int productID) {
        return Format.encode(ClassType.INVENTORY, "getItemByProductID", Integer.toString(productID));
    }
}    
