package server.services;

import server.models.Branch;
import server.models.SaleReport;
import server.models.customer.Customer;

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

    // Get all sale reports
    public List<SaleReport> getAllSaleReports() {
        return new ArrayList<>(saleReports); // Return a copy to prevent external modifications
    }

    public String addProductSale(int branchId ,int customerId, int productId, int amount) {
        Branch branch = BranchManager.getInstance().getBranchById(branchId);

        if (branch == null) {
            return "NOT FOUND BRANCH WITH ID " + customerId;
        }

        Customer customer = EmployeeManager.getInstance().getCustomers().getOrDefault(customerId, null);

        if (customer == null) {
            return "NOT FOUND CUSTOMER WITH ID " + customerId;
        }

         int stock = branch.getInventory().getOrDefault(productId, 0);

        if (amount > stock) {
            return String.format("not enough products , only %s remain", stock);
        }
        else {
            branch.getInventory().put(productId, stock - amount);
            customer.setTotalPurchases(customer.getTotalPurchases() + amount);

            return String.format("purchased success, only %s remain, customer spend total of %s products", branch.getInventory().get(productId), customer.getTotalPurchases());
        }
    }
}
