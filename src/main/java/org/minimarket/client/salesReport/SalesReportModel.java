package org.minimarket.client.salesReport;

public class SalesReportModel {
    private String productName;
    private int quantity;
    private double subtotal;

    public SalesReportModel(String productName, int quantity, double subtotal) {
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
