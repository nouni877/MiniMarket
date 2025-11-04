package org.minimarket.storageAccess;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SalesFileManager {

    private static final String SALES_LOG_FILE = "src/main/resources/data/sales_log.csv";
    private static final String TOTAL_SALES_FILE = "src/main/resources/data/sales.csv";

    // load sales
    public double loadTotalSales() {
        File file = new File(TOTAL_SALES_FILE);
        if (!file.exists()) return 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Double.parseDouble(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // save total sales
    public void saveTotalSales(double totalSales) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOTAL_SALES_FILE, StandardCharsets.UTF_8))) {
            writer.write(String.valueOf(totalSales));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add record
    public void logSale(String productName, int quantity, double subtotal) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_LOG_FILE, true))) {
            writer.write(productName + "," + quantity + "," + subtotal);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read all sale records
    public List<String[]> loadSalesLog() {
        List<String[]> records = new ArrayList<>();
        File file = new File(SALES_LOG_FILE);
        if (!file.exists()) return records;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) records.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
