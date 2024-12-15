package models;

import java.time.LocalDate;

public class SaleReport {
    private String branchID;
    private String productName;
    private String productCategory;
    private int quantitySold;
    private double totalSales;
    private LocalDate date;

    public SaleReport(String branchID, String productName, String productCategory, int quantitySold, double totalSales, LocalDate date) {
        this.branchID = branchID;
        this.productName = productName;
        this.productCategory = productCategory;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
        this.date = date;
    }

    // Getters and Setters
    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SaleReport{" +
                "branchID='" + branchID + '\'' +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", quantitySold=" + quantitySold +
                ", totalSales=" + totalSales +
                ", date=" + date +
                '}';
    }
}
