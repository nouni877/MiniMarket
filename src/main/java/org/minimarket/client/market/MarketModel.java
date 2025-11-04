package org.minimarket.client.market;

import org.minimarket.catalogue.Product;
import java.util.List;

public class MarketModel {
    private final List<Product> products;
    private double totalSales;

    public MarketModel(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() { return products; }
    public double getTotalSales() { return totalSales; }

    public void addSale(double saleAmount) {
        totalSales += saleAmount;
    }
}
