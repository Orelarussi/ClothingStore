package client.serverCommunication.decodeCMD;

import Store.Database.InventoryDAO;
import Store.Inventories.InventoryItem;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandInventory {
    public static String execute(String command) throws SQLException {
        InventoryDAO DAO = new InventoryDAO();
        List<InventoryItem> inventoryItems;
        String response = Format.encodeSuccessMessage();
        switch (Format.getMethod(command)) {
            case "getInventoryItemsByBranch":
                // public ArrayList<InventoryItem> getInventoryItemsByBranch(String branch) {
                inventoryItems = DAO.getInventoryItemsByBranch(Format.getFirstParam(command));
                if(inventoryItems.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeInventoryItems(inventoryItems);
                break;
            case "createNewItem":
                // public void createNewItem(InventoryItem item) {
                DAO.createNewItem(InventoryItem.deserializeFromString(Format.getFirstParam(command)));
                break;
            case "updateItem":
                // public void updateItem(InventoryItem item) {
                DAO.updateItem(InventoryItem.deserializeFromString(Format.getFirstParam(command)));
                break;
            case "deleteItem":
                // public void deleteItem(int productID) {
                DAO.deleteItem(Integer.parseInt(Format.getFirstParam(command)));
                break;
            case "getItemByProductID":
                // public InventoryItem getItemByProductID(int productID) {
                inventoryItems = DAO.getInventoryItemsByBranch(Format.getFirstParam(command));
                if(inventoryItems.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeInventoryItems(inventoryItems);
                break;
        }
        return response;
    }
}   

