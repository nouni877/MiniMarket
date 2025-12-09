package org.minimarket.client.market;

import org.minimarket.catalogue.Product;
import java.util.List;

/**
 * The MarketView class represents the **View** component in the MVC architecture
 * for the Mini Market system.
 *
 * Its responsibility is to display information to the user.
 * In this simplified console version, the view prints product data and sales
 * totals to the terminal. In the JavaFX version, this logic is replaced by UI
 * components, but the concept remains the same.
 */
public class MarketView {

    /**
     * Displays a formatted list of products to the console.
     *
     * @param products a list of Product objects to display
     */
    public void displayProducts(List<Product> products) {
        System.out.println("\n=== Product List ===");

        // Print each product with name, price, and stock level
        for (Product p : products) {
            System.out.printf("%s - £%.2f (%d in stock)%n",
                    p.getName(), p.getPrice(), p.getQuantity());
        }
    }

    /**
     * Outputs the total sales revenue to the console.
     *
     * @param total the total sales value to display
     */
    public void displayTotal(double total) {
        System.out.println("Total Sales: £" + total);
    }
}
