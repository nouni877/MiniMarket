package org.minimarket.client.salesReport;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.minimarket.catalogue.SaleRecord;
import org.minimarket.storageAccess.SalesFileManager;

import java.util.List;

public class SalesReportController {

    @FXML private TableView<SaleRecord> salesTable;
    @FXML private TableColumn<SaleRecord, String> colProduct;
    @FXML private TableColumn<SaleRecord, Integer> colQty;
    @FXML private TableColumn<SaleRecord, Double> colSubtotal;
    @FXML private Label lblTotal;

    private final SalesFileManager salesFileManager = new SalesFileManager();

    @FXML
    public void initialize() {
        assert salesTable != null : "fx:id=\"salesTable\" ";
        assert colProduct != null : "fx:id=\"colProduct\" ";
        assert colQty != null : "fx:id=\"colQty\"";
        assert colSubtotal != null : "fx:id=\"colSubtotal\" was not injected";

        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        loadSalesData();
    }

    private void loadSalesData() {
        List<SaleRecord> records = salesFileManager.loadSaleRecords();
        salesTable.setItems(FXCollections.observableArrayList(records));

        if (lblTotal != null) {
            double total = records.stream().mapToDouble(SaleRecord::getSubtotal).sum();
            lblTotal.setText(String.format("£%.2f", total));
        }
    }
}

