package server.models;

import java.time.LocalDate;

public class SaleReport {
    private int branchID;
    private int productId;
    private int quantitySold;
    private double totalSales;
    private LocalDate date;

    public SaleReport(int branchID, int productId, int quantitySold, double totalSales, LocalDate date) {
        this.branchID = branchID;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
        this.date = date;
    }

    // Getters and Setters
    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
                ", productId='" + productId + '\'' +
                ", quantitySold=" + quantitySold +
                ", totalSales=" + totalSales +
                ", date=" + date +
                '}';
    }
}
