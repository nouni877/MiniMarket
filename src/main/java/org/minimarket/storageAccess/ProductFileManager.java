package org.minimarket.storageAccess;

import org.minimarket.catalogue.Product;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductFileManager {
    private static final String PRODUCT_FILE = "src/main/resources/data/products.csv";

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(PRODUCT_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                products.add(new Product(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
        return products;
    }

    public static void saveProducts(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(PRODUCT_FILE), StandardCharsets.UTF_8))) {
            for (Product p : products) {
                bw.write(p.getName() + "," + p.getPrice() + "," + p.getQuantity());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }
}
