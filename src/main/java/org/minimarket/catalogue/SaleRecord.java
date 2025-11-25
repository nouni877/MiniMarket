package org.minimarket.catalogue;

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
