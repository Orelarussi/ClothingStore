package server.services;

import server.logger.Logger;
import server.models.Branch;
import server.models.SaleReport;
import server.models.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private static SalesManager instance;

    public static synchronized SalesManager getInstance() {
        if (instance == null) {
            instance = new SalesManager();
        }
        return instance;
    }

    private List<SaleReport> saleReports;

    public SalesManager() {
        this.saleReports = new ArrayList<>();
    }

    // Add a sale report
    public void addSaleReport(SaleReport saleReport) {
        saleReports.add(saleReport);
        System.out.println("Sale report added for product Id: " + saleReport.getProductId());
    }

    public void setSaleReports(List<SaleReport> saleReports) {
        this.saleReports = saleReports;
    }

    // Get all sale reports
    public List<SaleReport> getAllSaleReports() {
        System.out.println("Fetching all sale reports. Total reports: " + saleReports.size());
        return new ArrayList<>(saleReports); // Return a copy to prevent external modifications
    }

    public String addProductSale(int branchId ,int customerId, int productId, int amount) {
        Branch branch = BranchManager.getInstance().getBranchById(branchId);

        if (branch == null) {
            String error = "Branch with ID " + branchId + " not found.";
            System.out.println(error);
            return "NOT FOUND BRANCH WITH ID " + customerId;
        }

        Customer customer = EmployeeManager.getInstance().getCustomers().getOrDefault(customerId, null);

        if (customer == null) {
            String error = "Customer with ID " + customerId + " not found.";
            System.out.println(error);
            return "NOT FOUND CUSTOMER WITH ID " + customerId;
        }

         int stock = branch.getInventory().getOrDefault(productId, 0);

        if (amount > stock) {
            String error = "Not enough products in stock. Only " + stock + " remain.";
            System.out.println(error);
            return String.format("not enough products , only %s remain", stock);
        }
        else {
            branch.getInventory().put(productId, stock - amount);
            customer.setTotalPurchases(customer.getTotalPurchases() + amount);
            saleReports.add(new SaleReport(branch.getBranchID(), productId, amount, LocalDate.now()));

            String successMessage = String.format("Purchase successful. Remaining stock: %s. Customer total purchases: %s.",
                    branch.getInventory().get(productId), customer.getTotalPurchases());
            System.out.println(successMessage);
            Logger.log("Customer with ID "+customerId+" purchased "+amount+" units of Product ID "+productId+ " from Branch ID "+branchId+" successfully\n", Logger.LogType.SALE);
            return String.format("purchased success, only %s remain, customer spend total of %s products", branch.getInventory().get(productId), customer.getTotalPurchases());
        }
    }
}
