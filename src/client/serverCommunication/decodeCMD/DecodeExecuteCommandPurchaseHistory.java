package client.serverCommunication.decodeCMD;

import client.serverCommunication.Format;

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
                purchase = Purchase.deserializeFromString(command, 1);
                DAO.createNewPurchase(purchase);
                break;
            case "getItemsFromOrdersByBranchAndDays":
                String branch = Format.getFirstParam(command);
                int days = Integer.parseInt(Format.getSecondParam(command));
                List<PurchasedItem> temp = DAO.getItemsFromOrdersByBranchAndDays(branch, days);
                if(temp.size() == 0)
                    response = Format.encodeEmpty("אין נתונים עבור היום שנבחר");
                else
                    response = Format.encodePurchasedItems(temp);
                break;
        }
        return response;
    }
}
