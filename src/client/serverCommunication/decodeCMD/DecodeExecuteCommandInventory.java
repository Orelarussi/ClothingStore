package client.serverCommunication.decodeCMD;



import client.serverCommunication.Format;
import models.Product;
import services.InventoryManager;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandInventory {
    public static String execute(String command) throws SQLException {
        InventoryManager manager = new InventoryManager();
        List<Product> products;
        String response = Format.encodeSuccessMessage();
        String first = Format.getFirstParam(command);

        switch (Format.getMethod(command)) {
            case "getProductsByBranch":
                // public ArrayList<Product> getProductsByBranch(String branch) {
                products = manager.getProductsByBranch(first);
                if(products.isEmpty())
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeProducts(products);
                break;
            case "createNewItem":
                // public void createNewItem(Product item) {
                manager.addProduct(new Product(first));
                break;
            case "updateItem":
                // public void updateItem(Product item) {
                manager.updateItem(new Product(first));
                break;
            case "deleteItem":
                // public void deleteItem(int productID) {
                manager.removeProduct(Integer.parseInt(first));
                break;
            case "getItemByProductID":
                // public Product getItemByProductID(int productID) {
                products = manager.getProductsByBranch(first);
                if(products.isEmpty())
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeProducts(products);
                break;
        }
        return response;
    }
}   

