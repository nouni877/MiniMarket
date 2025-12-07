package org.minimarket.storageAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.minimarket.catalogue.Product;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * ProductFileManager handles loading, saving, and sharing the list of products
 * between different controllers (e.g., Buyer and Worker pages).
 * It maintains a single ObservableList that stays synchronized across the app.
 */
public class ProductFileManager {

    private static final String PRODUCT_FILE = "src/main/resources/data/products.csv";

    // Shared observable list — both pages use the same data
    private static final ObservableList<Product> products = FXCollections.observableArrayList();

    // Getter for shared product list
    public static ObservableList<Product> getProducts() {
        return products;
    }

    /**
     * Loads products from CSV into the shared ObservableList.
     * Clears previous data before reloading.
     */
    public static void loadProducts() {
        products.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(PRODUCT_FILE), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Expecting 4 columns: name, price, quantity, category
                if (parts.length == 4) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String category = parts[3];

                    products.add(new Product(name, price, quantity, category));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Saves all products from the ObservableList to the CSV file.
     */
    public static void saveProducts() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(PRODUCT_FILE), StandardCharsets.UTF_8))) {

            for (Product p : products) {
                bw.write(p.getName() + "," +
                        p.getPrice() + "," +
                        p.getQuantity() + "," +
                        p.getCategory());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    /**
     * Adds a product to the shared list and saves immediately.
     */
    public static void addProduct(Product product) {
        products.add(product);
        saveProducts();
    }

    /**
     * Removes a product from the shared list and saves immediately.
     */
    public static void removeProduct(Product product) {
        products.remove(product);
        saveProducts();
    }
}
