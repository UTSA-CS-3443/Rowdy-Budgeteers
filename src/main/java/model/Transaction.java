package main.java.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {

    private String transactionID;
    private String purchaseName;
    private String purchaseType;
    private String date;
    private double cost;

    public Transaction(String transactionID, String purchaseName,
                       String purchaseType, String date, double cost) {

        this.transactionID = transactionID;
        this.purchaseName = purchaseName;
        this.purchaseType = purchaseType;
        this.date = date;
        this.cost = cost;
    }

    // UML: validateTransaction(): boolean
    public boolean validateTransaction() {
        return transactionID != null && !transactionID.trim().isEmpty()
                && purchaseName != null && !purchaseName.trim().isEmpty()
                && purchaseType != null && !purchaseType.trim().isEmpty()
                && date != null
                && cost > 0;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getTransactionDetails() {
        return purchaseName + " - " + purchaseType
                + " - $" + cost + " - " + date;
    }

    public void updateTransaction(double cost) {
        if (cost > 0) {
            this.cost = cost;
        }
    }
}
