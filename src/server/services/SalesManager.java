package server.services;

import server.logger.Logger;
import server.models.Branch;
import server.models.SaleReport;
import server.models.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static server.logger.Logger.log;

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
        String s = "Fetching all sale reports. Total reports: " + saleReports.size();
        System.out.println(s);
        log(s);
        return new ArrayList<>(saleReports); // Return a copy to prevent external modifications
    }

    public String addProductSale(int branchId ,int customerId, int productId, int amount) {
        Branch branch = BranchManager.getInstance().getBranchById(branchId);

        if (branch == null) {
            String error = "Branch with ID " + branchId + " not found.";
            System.out.println(error);
            log(error);
            return "NOT FOUND BRANCH WITH ID " + customerId;
        }

        Customer customer = EmployeeManager.getInstance().getCustomers().getOrDefault(customerId, null);

        if (customer == null) {
            String error = "Customer with ID " + customerId + " not found.";
            System.out.println(error);
            log(error);
            return "NOT FOUND CUSTOMER WITH ID " + customerId;
        }

         int stock = branch.getInventory().getOrDefault(productId, 0);

        if (amount > stock) {
            String error = "Not enough products in stock. Only " + stock + " remain.";
            System.out.println(error);
            log(error);
            return error;
        }
        else {
            branch.getInventory().put(productId, stock - amount);
            customer.setTotalPurchases(customer.getTotalPurchases() + amount);
            saleReports.add(new SaleReport(branch.getBranchID(), productId, amount, LocalDate.now()));

            String successMessage = String.format("Purchase successful. Remaining stock: %s. Customer total purchases: %s.",
                    branch.getInventory().get(productId), customer.getTotalPurchases());
            System.out.println(successMessage);
            log(successMessage);
            return successMessage;
        }
    }
}
