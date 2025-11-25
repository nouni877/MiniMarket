package org.minimarket.storageAccess;

import org.minimarket.catalogue.SaleRecord;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading of sales records and total sales.
 */
public class SalesFileManager {

    private static final String SALES_FILE = "src/main/resources/data/total_sales.txt";
    private static final String SALES_LOG_FILE = "src/main/resources/data/sales_log.csv";

    /**
     * Loads total sales from text file.
     */
    public double loadTotalSales() {
        double total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(SALES_FILE))) {
            String line = br.readLine();
            if (line != null) total = Double.parseDouble(line);
        } catch (IOException e) {
            System.err.println("Error reading sales file: " + e.getMessage());
        }
        return total;
    }

    /**
     * Saves total sales to text file.
     */
    public void saveTotalSales(double totalSales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SALES_FILE))) {
            pw.println(totalSales);
        } catch (IOException e) {
            System.err.println("Error saving total sales: " + e.getMessage());
        }
    }

    /**
     * Appends a list of sold products to the sales log.
     */
    public void saveSaleRecords(List<SaleRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(SALES_LOG_FILE, true), StandardCharsets.UTF_8))) {

            for (SaleRecord record : records) {
                bw.write(record.getProductName() + "," + record.getQuantity() + "," + record.getSubtotal());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error writing sales log: " + e.getMessage());
        }
    }

    /**
     * Loads all sale records from CSV.
     */
    public List<SaleRecord> loadSaleRecords() {
        List<SaleRecord> sales = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(SALES_LOG_FILE), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    sales.add(new SaleRecord(parts[0],
                            Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sales log: " + e.getMessage());
        }
        return sales;
    }
}

