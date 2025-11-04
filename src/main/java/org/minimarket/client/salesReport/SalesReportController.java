package org.minimarket.client.salesReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.minimarket.catalogue.SaleRecord;
import org.minimarket.storageAccess.SalesFileManager;

import java.util.List;

public class SalesReportController {

    @FXML private TableView<SaleRecord> tblSales;
    @FXML private TableColumn<SaleRecord, String> colProduct;
    @FXML private TableColumn<SaleRecord, Integer> colQty;
    @FXML private TableColumn<SaleRecord, Double> colSubtotal;

    private final SalesFileManager salesFileManager = new SalesFileManager();

    @FXML
    public void initialize() {
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        List<String[]> salesData = salesFileManager.loadSalesLog();
        ObservableList<SaleRecord> records = FXCollections.observableArrayList();

        for (String[] row : salesData) {
            records.add(new SaleRecord(row[0], Integer.parseInt(row[1]), Double.parseDouble(row[2])));
        }

        tblSales.setItems(records);
    }
}
