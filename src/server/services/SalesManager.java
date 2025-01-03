package server.services;

import models.SaleReport;

import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private List<SaleReport> saleReports;

    public SalesManager() {
        this.saleReports = new ArrayList<>();
    }

    // Add a sale report
    public void addSaleReport(SaleReport saleReport) {
        saleReports.add(saleReport);
        System.out.println("Sale report added for product: " + saleReport.getProductName());
    }

    // Get all sale reports
    public List<SaleReport> getAllSaleReports() {
        return new ArrayList<>(saleReports); // Return a copy to prevent external modifications
    }
}
