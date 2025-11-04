package org.minimarket.client.market;

import org.minimarket.catalogue.Product;
import java.util.List;

public class MarketView {
    public void displayProducts(List<Product> products) {
        System.out.println("\n=== Product List ===");
        for (Product p : products) {
            System.out.printf("%s - £%.2f (%d in stock)%n", p.getName(), p.getPrice(), p.getQuantity());
        }
    }

    public void displayTotal(double total) {
        System.out.println("Total Sales: £" + total);
    }
}
