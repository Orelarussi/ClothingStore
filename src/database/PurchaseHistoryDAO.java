package database;

import client.serverCommunication.Format;
import logger.Logger;
import models.Product;
import models.Purchase;
import models.purchaseHistory.PurchasedItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.*;


public class PurchaseHistoryDAO extends GeneralDAO {

    public void createNewPurchase(Purchase purchase) throws SQLException {
        // Implementation of the insertObject method should be in GeneralDAO
        insertObject("PurchaseHistory", queryForInsert(purchase));  
        int orderID = -1;
        ResultSet rs = getObject("PurchaseHistory", "TOP 1 *", "CustomerID=" + purchase.getCustomerID() + " ORDER BY date DESC");
        rs.next();
        orderID = Integer.parseInt(rs.getString("PurchaseID"));
    

        List<Product> items = purchase.getItems();
        for( int i=0; i < items.size(); i++) {
            PurchasedItem item = new PurchasedItem(orderID, items.get(i).getId());
            insertObject("[PurchaseHistoryItems]", queryForInsertItems(item));
        }
        Logger.logPurchase(purchase);
    }

    private String queryForInsert(Purchase purchase) {
        String query = String.format("VALUES (%d, CONVERT(datetime, '%s', 103), N'%s')",
                purchase.getCustomerID(),
                Format.dateToString(purchase.getDate()),
                purchase.getBranch());
        return query;
    }

    private String queryForInsertItems(PurchasedItem item) {
        String query = String.format("VALUES (%d, %d)",
                item.getPurchaseID(),
                item.getProductID());

        return query;
    }

    // Additional helper method to convert ResultSet to Product objects
    public List<PurchasedItem> getItemsFromOrdersByBranchAndDays(String branch, int days) throws SQLException {
        ResultSet res;
        List<Purchase> orders;
        List<PurchasedItem> purchasedItems = new ArrayList<PurchasedItem>();
        
        if( days == 0 )
            res = getObject("PurchaseHistory", "*", "Branch = N'" + branch + "' AND cast(date as Date) = cast(getdate() as Date)"); 
        else
            res = getObject("PurchaseHistory", "*", "Branch = N'" + branch + "' AND ( cast(date as DATE) >= DATEADD(day, -" + days + ", GETDATE()) AND ( cast(date as DATE) <= GETDATE()))");
        
        orders = resToCollection(res);
        for(int i=0; i < orders.size(); i++) {
            List<PurchasedItem> temp = getItemsByPurchaseID(orders.get(i).getPurchaseID());
            for(int j=0; j < temp.size(); j++) 
                purchasedItems.add(temp.get(j));
        }
        return purchasedItems;
    }

    private List<Purchase> resToCollection(ResultSet res) throws SQLException {
        List<Purchase> resArray = new ArrayList<>();
        while(res.next())
        {
            int purchaseID = Integer.parseInt(res.getString("PurchaseID"));
            int customerID = Integer.parseInt(res.getString("CustomerID"));
            LocalDateTime date = Format.stringToDateDB(res.getString("Date"));
            String branch = res.getString("Branch");
            Purchase temp = new Purchase(purchaseID, customerID, date, branch); 
            resArray.add(temp);
        }
        return resArray;
    }

    private List<PurchasedItem> getItemsByPurchaseID(int purchaseID) throws SQLException {
        ResultSet res = getObject("PurchaseHistoryItems", "*", "PurchaseID=" + purchaseID); 
        return resToItemsCollection(res);
    }

    private List<PurchasedItem> resToItemsCollection(ResultSet res) throws SQLException {
        List<PurchasedItem> resArray = new ArrayList<>();
        while(res.next())
        {
            int purchaseID = Integer.parseInt(res.getString("PurchaseID"));
            int productID = Integer.parseInt(res.getString("ProductID"));

            PurchasedItem temp = new PurchasedItem(purchaseID, productID); 
            resArray.add(temp);
        }
        return resArray;
    }
}

