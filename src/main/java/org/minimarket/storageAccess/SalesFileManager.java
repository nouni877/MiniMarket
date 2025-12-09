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

    private static final String SALES_FILE_RESOURCE = "src/main/resources/data/total_sales.txt";
    private static final String SALES_LOG_RESOURCE = "src/main/resources/data/sales_log.csv";

    private static final String SALES_FILE_EXTERNAL = "total_sales.txt";
    private static final String SALES_LOG_EXTERNAL = "sales_log.csv";

    /**
     * Loads total sales from the resource file.
     */
    public double loadTotalSales() {
        double total = 0;

        try (InputStream is = SalesFileManager.class.getResourceAsStream(SALES_FILE_RESOURCE)) {

            if (is == null) {
                System.err.println("WARNING: total_sales.txt not found in resources.");
                return 0;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line != null) total = Double.parseDouble(line);

        } catch (Exception e) {
            System.err.println("Error loading total sales: " + e.getMessage());
        }

        return total;
    }

    /**
     * Saves total sales to an external writable file.
     */
    public void saveTotalSales(double totalSales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SALES_FILE_EXTERNAL))) {
            pw.println(totalSales);
        } catch (IOException e) {
            System.err.println("Error saving total sales: " + e.getMessage());
        }
    }

    /**
     * Loads sale records from the resource CSV file.
     */
    public List<SaleRecord> loadSaleRecords() {
        List<SaleRecord> sales = new ArrayList<>();

        try (InputStream is = SalesFileManager.class.getResourceAsStream(SALES_LOG_RESOURCE)) {

            if (is == null) {
                System.err.println("WARNING: sales_log.csv not found in resources.");
                return sales;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    sales.add(new SaleRecord(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2])
                    ));
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading sales log: " + e.getMessage());
        }

        return sales;
    }

    /**
     * Appends new sales to an external writable CSV log.
     */
    public void saveSaleRecords(List<SaleRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(SALES_LOG_EXTERNAL, true))) {

            for (SaleRecord record : records) {
                bw.write(record.getProductName() + "," +
                        record.getQuantity() + "," +
                        record.getSubtotal());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error writing sales log: " + e.getMessage());
        }
    }
}
