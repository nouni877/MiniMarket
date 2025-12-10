package org.minimarket.utility;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class responsible for generating transaction receipts.
 * The receipt is created as a timestamped .txt file and stored inside
 * a dedicated "receipts" folder. This class is used by the BuyerController
 * after successful checkout.
 */
public class ReceiptGenerator {

    /**
     * Creates a receipt file using item strings from the cart.
     * Each item is formatted as: "Product Name - £Price"
     *
     * @param items A list of cart item strings representing purchased items.
     * @param total The total cost of the transaction.
     * @return The file path of the generated receipt.
     */
    public static String createReceiptFromStrings(List<String> items, double total) {
        String filename = "";

        try {
            // Create receipts folder if it does not exist
            File folder = new File("receipts");
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Create a timestamp for file naming (ensures unique receipts)
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            // Full file path for the new receipt
            filename = "receipts/receipt_" + timestamp + ".txt";

            // Write receipt content to file
            FileWriter writer = new FileWriter(filename);

            writer.write("========== MINI MARKET RECEIPT ==========\n");
            writer.write("Date: " + LocalDateTime.now() + "\n\n");
            writer.write(String.format("%-20s %-5s %-10s\n", "Item", "Qty", "Subtotal (£)"));
            writer.write("--------------------------------------------\n");

            // Process each cart item
            for (String entry : items) {
                try {
                    String name = entry.substring(0, entry.lastIndexOf(" - £"));
                    double price = Double.parseDouble(entry.substring(entry.lastIndexOf("£") + 1));
                    // Write one line per purchased item
                    writer.write(String.format("%-20s %-5d %-10.2f\n", name, 1, price));

                } catch (Exception e) {
                    // If formatting is invalid, note it but continue
                    writer.write("INVALID ITEM FORMAT\n");
                }
            }

            writer.write("--------------------------------------------\n");
            writer.write(String.format("TOTAL: £%.2f\n", total));
            writer.write("============================================\n\n");
            writer.write("Thank you for shopping at Mini Market!\n");

            writer.close();

            System.out.println("Receipt saved: " + filename);

        } catch (Exception e) {
            System.err.println("Error generating receipt: " + e.getMessage());
        }

        return filename;
    }
}

