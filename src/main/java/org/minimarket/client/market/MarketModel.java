package org.minimarket.client.market;

import org.minimarket.catalogue.Product;
import java.util.List;
/**
 * The MarketModel class represents the data layer for the Mini Market system.
 * It stores the list of available products and tracks total sales made
 * during the lifetime of the application session.
 *
 * This class is part of the MVC architecture:
 * - Model  → stores and manages data
 * - View   → displays information to the user
 * - Controller → handles interactions and updates the Model
 */
public class MarketModel {
    private final List<Product> products;
    private double totalSales;

    public MarketModel(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() { return products; }
    public double getTotalSales() { return totalSales; }
    /**
     * Adds a sale amount to the total sales counter.
     * Called whenever a customer completes a purchase.
     */
    public void addSale(double saleAmount) {
        totalSales += saleAmount;
    }
}
