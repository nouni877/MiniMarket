package org.minimarket.client.market;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.minimarket.catalogue.CartItem;
import org.minimarket.catalogue.Product;
import org.minimarket.storageAccess.ProductFileManager;
import org.minimarket.storageAccess.SalesFileManager;
import org.minimarket.utility.SoundManager;

import java.util.Optional;

public class MarketController {

    // fxml elements
    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    @FXML private TextField txtSearch, txtAddName, txtAddPrice, txtAddQty;
    @FXML private CheckBox chkLowStock;
    @FXML private Label lblTotalSales;

    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> cartName;
    @FXML private TableColumn<CartItem, Integer> cartQty;
    @FXML private TableColumn<CartItem, Double> cartSubtotal;
    @FXML private TextField txtCartName, txtCartQty;
    @FXML private Label lblCartTotal;

    @FXML private TextField txtEditName, txtEditPrice, txtEditQty;
    @FXML private TextField txtRemoveName;

    // Role-based access buttons
    @FXML private Button btnAddProduct, btnEditProduct, btnRemoveProduct, btnClearInventory;

    private ObservableList<Product> products;
    private ObservableList<CartItem> cartItems;

    private final ProductFileManager productFileManager = new ProductFileManager();
    private final SalesFileManager salesFileManager = new SalesFileManager();
    private final SoundManager soundManager = new SoundManager();

    private double totalSales = 0.0;
    private String userRole = "buyer"; // default

    @FXML
    public void initialize() {
        // Setup product table
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Setup cart table
        cartName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        products = FXCollections.observableArrayList(productFileManager.loadProducts());
        tblProducts.setItems(products);

        cartItems = FXCollections.observableArrayList();
        tblCart.setItems(cartItems);

        totalSales = salesFileManager.loadTotalSales();
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        lblCartTotal.setText("Cart Total: £0.00");
    }

    public void setUserRole(String role) {
        this.userRole = role;

        if (role.equalsIgnoreCase("buyer")) {
            disableWorkerFeatures();
        } else {
            enableWorkerFeatures();
        }
    }

    private void disableWorkerFeatures() {
        btnAddProduct.setDisable(true);
        btnEditProduct.setDisable(true);
        btnRemoveProduct.setDisable(true);
        btnClearInventory.setDisable(true);

        txtAddName.setDisable(true);
        txtAddPrice.setDisable(true);
        txtAddQty.setDisable(true);
        txtEditName.setDisable(true);
        txtEditPrice.setDisable(true);
        txtEditQty.setDisable(true);
        txtRemoveName.setDisable(true);
    }

    private void enableWorkerFeatures() {
        btnAddProduct.setDisable(false);
        btnEditProduct.setDisable(false);
        btnRemoveProduct.setDisable(false);
        btnClearInventory.setDisable(false);
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            tblProducts.setItems(products);
        } else {
            ObservableList<Product> filtered = FXCollections.observableArrayList();
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(keyword)) filtered.add(p);
            }
            tblProducts.setItems(filtered);
        }
    }

    @FXML
    private void handleClearSearch(ActionEvent e) {
        txtSearch.clear();
        tblProducts.setItems(products);
    }

    @FXML
    private void handleLowStockFilter(ActionEvent e) {
        if (chkLowStock.isSelected()) {
            ObservableList<Product> lowStock = FXCollections.observableArrayList();
            for (Product p : products) if (p.getQuantity() < 5) lowStock.add(p);
            tblProducts.setItems(lowStock);
        } else {
            tblProducts.setItems(products);
        }
    }

    @FXML
    private void handleAddProduct(ActionEvent e) {
        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can add products.");
            return;
        }

        try {
            String name = txtAddName.getText().trim();
            double price = Double.parseDouble(txtAddPrice.getText());
            int qty = Integer.parseInt(txtAddQty.getText());

            if (name.isEmpty() || price <= 0 || qty < 0) {
                showAlert("Invalid input", "Please enter valid product details.");
                soundManager.playErrorSound();
                return;
            }

            products.add(new Product(name, price, qty));
            productFileManager.saveProducts(products);
            clearAddFields();
            soundManager.playSuccessSound();
            showAlert("Success", "Product added successfully!");
        } catch (Exception ex) {
            showAlert("Error", "Invalid product input format.");
            soundManager.playErrorSound();
        }
    }

    private boolean isWorker() {
        return userRole.equalsIgnoreCase("worker");
    }

    private void clearAddFields() {
        txtAddName.clear();
        txtAddPrice.clear();
        txtAddQty.clear();
    }

    @FXML
    private void handleAddToCart(ActionEvent e) {
        String name = txtCartName.getText().trim();
        int qty;

        try {
            qty = Integer.parseInt(txtCartQty.getText());
        } catch (Exception ex) {
            showAlert("Invalid quantity", "Enter a valid number.");
            soundManager.playErrorSound();
            return;
        }

        Product selected = findProductByName(name);
        if (selected == null) {
            showAlert("Not found", "Product does not exist.");
            soundManager.playErrorSound();
            return;
        }

        if (selected.getQuantity() < qty) {
            showAlert("Insufficient stock", "Not enough items available.");
            soundManager.playErrorSound();
            return;
        }

        selected.setQuantity(selected.getQuantity() - qty);
        cartItems.add(new CartItem(name, qty, selected.getPrice() * qty));
        updateCartTotal();
        productFileManager.saveProducts(products);
        soundManager.playClickSound();
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent e) {
        CartItem selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartItems.remove(selected);
            updateCartTotal();
            soundManager.playClickSound();
        }
    }

    @FXML
    private void handleCheckout(ActionEvent e) {
        double cartTotal = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        totalSales += cartTotal;
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        salesFileManager.saveTotalSales(totalSales);
        cartItems.clear();
        updateCartTotal();
        soundManager.playSuccessSound();
        showAlert("Purchase Complete", "Thank you for your purchase!");
    }

    private void updateCartTotal() {
        double total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        lblCartTotal.setText(String.format("Cart Total: £%.2f", total));
    }

    private Product findProductByName(String name) {
        for (Product p : products) if (p.getName().equalsIgnoreCase(name)) return p;
        return null;
    }

    @FXML
    private void handleEditProduct(ActionEvent e) {
        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can edit products.");
            return;
        }

        String name = txtEditName.getText().trim();
        Product target = findProductByName(name);
        if (target == null) {
            showAlert("Error", "Product not found.");
            soundManager.playErrorSound();
            return;
        }

        try {
            double newPrice = txtEditPrice.getText().isEmpty() ? target.getPrice() : Double.parseDouble(txtEditPrice.getText());
            int newQty = txtEditQty.getText().isEmpty() ? target.getQuantity() : Integer.parseInt(txtEditQty.getText());

            target.setPrice(newPrice);
            target.setQuantity(newQty);

            productFileManager.saveProducts(products);
            tblProducts.refresh();
            txtEditName.clear();
            txtEditPrice.clear();
            txtEditQty.clear();
            soundManager.playSuccessSound();
            showAlert("Success", "Product updated successfully!");
        } catch (Exception ex) {
            showAlert("Invalid Input", "Please enter valid values.");
            soundManager.playErrorSound();
        }
    }

    @FXML
    private void handleRemoveProduct(ActionEvent e) {
        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can remove products.");
            return;
        }

        String name = txtRemoveName.getText().trim();
        Product target = findProductByName(name);
        if (target == null) {
            showAlert("Not found", "No such product.");
            soundManager.playErrorSound();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + name + "?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            products.remove(target);
            productFileManager.saveProducts(products);
            tblProducts.refresh();
            txtRemoveName.clear();
            soundManager.playClickSound();
            showAlert("Deleted", "Product removed successfully.");
        }
    }

    @FXML
    private void handleClearInventory(ActionEvent e) {
        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can clear inventory.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Clear all products?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            products.clear();
            productFileManager.saveProducts(products);
            tblProducts.refresh();
            soundManager.playClickSound();
            showAlert("Inventory Cleared", "All products have been removed.");
        }
    }

    @FXML
    private void handleViewReport(ActionEvent e) {
        try {
            Stage reportStage = new Stage();
            new org.minimarket.client.salesReport.SalesReportView().start(reportStage);
        } catch (Exception ex) {
            showAlert("Error", "Failed to open report window.");
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
