package client.serverCommunication.encodeCMD;

import Store.Inventories.InventoryItem;

public class EncodeCommandInventory {
    public static String getInventoryItemsByBranch(String branch) {
        return Format.encode(ClassType.INVENTORY, "getInventoryItemsByBranch", branch);
    }
    
    public static String createNewItem(InventoryItem item) {
        return Format.encode(ClassType.INVENTORY, "createNewItem", item.serializeToString());
    }

    public static String updateItem(InventoryItem item) {
        return Format.encode(ClassType.INVENTORY, "updateItem" , item.serializeToString());
    }

    public static String deleteItem(int productID) {
        return Format.encode(ClassType.INVENTORY, "deleteItem", Integer.toString(productID));
    }

    public static String getItemByProductID(int productID) {
        return Format.encode(ClassType.INVENTORY, "getItemByProductID", Integer.toString(productID));
    }
}    
