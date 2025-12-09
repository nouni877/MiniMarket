package org.minimarket.catalogue;
/**
 * Represents a single item inside the customer's shopping cart.
 * Each CartItem stores the product name, the quantity purchased,
 * and the subtotal cost for that product (price * quantity).
 */
public class CartItem {
    /** Name of the product being purchased */
    private String productName;
    /** Number of units of the product added to the cart */
    private int quantity;
    /** Total price for this cart item (quantity × unit price) */
    private double subtotal;

    public CartItem(String productName, int quantity, double subtotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    /**
     * Returns the subtotal cost for this specific product entry
     * Used when calculating the total price in the shopping cart
     */
    public double getSubtotal() { return subtotal; }

}
