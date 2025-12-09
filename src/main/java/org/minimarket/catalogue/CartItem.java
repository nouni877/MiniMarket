package org.minimarket.catalogue;

public class CartItem {
    private String productName;
    private int quantity;
    private double subtotal;

    public CartItem(String productName, int quantity, double subtotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    public double getSubtotal() { return subtotal; }

}
