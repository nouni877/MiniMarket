package org.minimarket.storageAccess;

import org.minimarket.catalogue.SaleRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * SalesFileManager handles all file-based persistence for sales data.
 *
 * It is responsible for:
 *  - Saving and loading the total amount of all sales made.
 *  - Appending individual sale records to a CSV sales log.
 *  - Loading previously stored sale records for reporting.
 *
 * This class acts as the **data access layer**, keeping file I/O logic separate
 * from the UI and business logic (following MVC and separation-of-concerns principles).
 */
public class SalesFileManager {

    /** CSV file storing detailed sale records (product, qty, subtotal). */
    private static final String SALES_LOG_FILE = "src/main/resources/data/sales_log.csv";

    /** Text file storing a single total sales value. */
    private static final String SALES_FILE = "src/main/resources/data/total_sales.txt";

    /**
     * Loads the total sales figure from the text file.
     * If the file does not exist, the system starts with total = 0.
     *
     * @return the total sales value stored in the file
     */
    public double loadTotalSales() {
        double total = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(SALES_FILE))) {
            String line = br.readLine();
            if (line != null) total = Double.parseDouble(line);
        } catch (IOException e) {
            System.out.println("Total sales file not found, starting at 0.");
        }

        return total;
    }

    /**
     * Saves the total sales value to the text file.
     * This overwrites any previous value.
     *
     * @param totalSales the updated total sales amount
     */
    public void saveTotalSales(double totalSales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SALES_FILE))) {
            pw.println(totalSales);
        } catch (IOException e) {
            System.err.println("Error saving total sales: " + e.getMessage());
        }
    }

    /**
     * Appends new sale records to the CSV sales log.
     * Each sale record is written on a new line in the format:
     *
     *      productName,quantity,subtotal
     *
     * @param records list of SaleRecord objects to save
     */
    public void saveSaleRecords(List<SaleRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(SALES_LOG_FILE, true), StandardCharsets.UTF_8))) {

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

    /**
     * Loads all sale records from the CSV file into a list.
     * This is used by the Sales Report screen to display a full history of sales.
     *
     * @return a list of SaleRecord objects representing all logged sales
     */
    public List<SaleRecord> loadSaleRecords() {
        List<SaleRecord> sales = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(SALES_LOG_FILE), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    sales.add(new SaleRecord(
                            parts[0],                         // product name
                            Integer.parseInt(parts[1]),       // quantity
                            Double.parseDouble(parts[2])      // subtotal
                    ));
                }
            }

        } catch (IOException e) {
            System.out.println("Sales log file missing, no previous sales.");
        }

        return sales;
    }
}

