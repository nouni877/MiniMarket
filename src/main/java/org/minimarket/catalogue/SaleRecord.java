package org.minimarket.catalogue;
/**
 * Represents a single completed sale entry recorded in the system.
 * A SaleRecord stores which product was sold, how many units were sold,
 * and the total revenue generated from that sale.
 */
public class SaleRecord {
    private String productName;
    private int quantity;
    private double subtotal;

    public SaleRecord(String productName, int quantity, double subtotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
