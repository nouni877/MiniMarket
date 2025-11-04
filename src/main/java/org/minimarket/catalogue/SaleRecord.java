package org.minimarket.catalogue;

import java.time.LocalDateTime;

public class SaleRecord {
    private String productName;
    private int quantity;
    private double total;
    private LocalDateTime dateTime;

    public SaleRecord(String productName, int quantity, double total) {
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
        this.dateTime = LocalDateTime.now();
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
    public LocalDateTime getDateTime() { return dateTime; }
}
