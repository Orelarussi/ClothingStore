package server.models;

import server.utils.JsonSerializable;

import java.time.LocalDate;

public class SaleReport extends JsonSerializable {
    private int branchID;
    private int productId;
    private int quantitySold;
    private LocalDate date;

    public SaleReport(int branchID, int productId, int quantitySold, LocalDate date) {
        this.branchID = branchID;
        this.productId = productId;
        this.quantitySold = quantitySold;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SaleReport {" +
                "branchID='" + branchID + '\'' +
                ", productId='" + productId + '\'' +
                ", quantitySold=" + quantitySold +
                ", date=" + date +
                '}';
    }

    @Override
    protected void populateFromJson(String json) {
        SaleReport temp = gson.fromJson(json, SaleReport.class);

        this.branchID = temp.getBranchID();
        this.productId = temp.getProductId();
        this.quantitySold = temp.quantitySold;
        this.date = temp.date;
    }
}
