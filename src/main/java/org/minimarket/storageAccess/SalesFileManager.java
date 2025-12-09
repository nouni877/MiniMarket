package org.minimarket.storageAccess;

import org.minimarket.catalogue.SaleRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SalesFileManager {

    private static final String SALES_LOG_FILE = "src/main/resources/data/sales_log.csv";
    private static final String SALES_FILE = "src/main/resources/data/total_sales.txt";



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

    public void saveTotalSales(double totalSales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SALES_FILE))) {
            pw.println(totalSales);
        } catch (IOException e) {
            System.err.println("Error saving total sales: " + e.getMessage());
        }
    }

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

    public List<SaleRecord> loadSaleRecords() {
        List<SaleRecord> sales = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(SALES_LOG_FILE), StandardCharsets.UTF_8))) {

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

        } catch (IOException e) {
            System.out.println("Sales log file missing, no previous sales.");
        }

        return sales;
    }
}

