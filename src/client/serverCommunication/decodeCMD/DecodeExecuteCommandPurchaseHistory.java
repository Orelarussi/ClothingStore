package client.serverCommunication.decodeCMD;

import client.serverCommunication.Format;
import database.PurchaseHistoryDAO;
import models.Purchase;
import models.purchaseHistory.PurchasedItem;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandPurchaseHistory {
    public static String execute(String command) throws SQLException
    {
        PurchaseHistoryDAO DAO = new PurchaseHistoryDAO();
        Purchase purchase;
        String response = Format.encodeSuccessMessage();

        switch(Format.getMethod(command)) {
            case "createNewPurchase":
                purchase = new Purchase(command);
                DAO.createNewPurchase(purchase);
                break;
            case "getItemsFromOrdersByBranchAndDays":
                String branch = Format.getFirstParam(command);
                int days = Integer.parseInt(Format.getSecondParam(command));
                List<PurchasedItem> temp = DAO.getItemsFromOrdersByBranchAndDays(branch, days);
                if(temp.isEmpty())
                    response = Format.encodeEmpty("אין נתונים עבור היום שנבחר");
                else
                    response = Format.encodePurchasedItems(temp);
                break;
        }
        return response;
    }
}
