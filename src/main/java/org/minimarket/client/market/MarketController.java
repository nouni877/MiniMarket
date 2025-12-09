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

public class MarketController {

    // === FXML elements ===
    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    @FXML private TextField txtSearch;

    @FXML private TextField txtAddName;
    @FXML private TextField txtAddPrice;
    @FXML private TextField txtAddQty;
    @FXML private TextField txtAddCategory;

    @FXML private TextField txtEditName;
    @FXML private TextField txtEditPrice;
    @FXML private TextField txtEditQty;
    @FXML private TextField txtEditCategory;

    @FXML private TextField txtRemoveName;

    @FXML private CheckBox chkLowStock;
    @FXML private Label lblTotalSales;

    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> cartName;
    @FXML private TableColumn<CartItem, Integer> cartQty;
    @FXML private TableColumn<CartItem, Double> cartSubtotal;

    @FXML private TextField txtCartName;
    @FXML private TextField txtCartQty;
    @FXML private Label lblCartTotal;

    @FXML private Button btnAddProduct;
    @FXML private Button btnEditProduct;
    @FXML private Button btnRemoveProduct;
    @FXML private Button btnClearInventory;


    private ObservableList<Product> products;
    private ObservableList<CartItem> cartItems;
    private SoundManager soundManager;


    private final SalesFileManager salesFileManager = new SalesFileManager();

    private double totalSales = 0.0;
    private String userRole = "buyer";  // default role

    // === Initialization ===
    @FXML
    public void initialize() {

        // Product Table Columns
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Cart Table Columns
        cartName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Load products from CSV
        ProductFileManager.loadProducts();
        products = ProductFileManager.getProducts();
        tblProducts.setItems(products);

        // Cart
        cartItems = FXCollections.observableArrayList();
        tblCart.setItems(cartItems);

        // Load total sales
        totalSales = salesFileManager.loadTotalSales();
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        lblCartTotal.setText("Cart Total: £0.00");
    }
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    // Role Handling
    public void setUserRole(String role) {
        this.userRole = role;

        boolean isBuyer = role.equalsIgnoreCase("buyer");

        // Disable Worker Controls for Buyer
        btnAddProduct.setDisable(isBuyer);
        btnEditProduct.setDisable(isBuyer);
        btnRemoveProduct.setDisable(isBuyer);
        btnClearInventory.setDisable(isBuyer);

        txtAddName.setDisable(isBuyer);
        txtAddPrice.setDisable(isBuyer);
        txtAddQty.setDisable(isBuyer);
        txtAddCategory.setDisable(isBuyer);

        txtEditName.setDisable(isBuyer);
        txtEditPrice.setDisable(isBuyer);
        txtEditQty.setDisable(isBuyer);
        txtEditCategory.setDisable(isBuyer);

        txtRemoveName.setDisable(isBuyer);
    }

    private boolean isWorker() {
        return userRole.equalsIgnoreCase("worker");
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            tblProducts.setItems(products);
            return;
        }

        ObservableList<Product> filtered = FXCollections.observableArrayList();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword)) {
                filtered.add(p);
            }
        }

        tblProducts.setItems(filtered);
    }

    @FXML
    private void handleClearSearch(ActionEvent e) {
        txtSearch.clear();
        tblProducts.setItems(products);
    }

    // Low Stock Filter
    @FXML
    private void handleLowStockFilter(ActionEvent e) {
        if (chkLowStock.isSelected()) {
            ObservableList<Product> lowStock = FXCollections.observableArrayList();

            for (Product p : products) {
                if (p.getQuantity() < 5) lowStock.add(p);
            }

            tblProducts.setItems(lowStock);
        } else {
            tblProducts.setItems(products);
        }
    }

    // Add Product
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
            String category = txtAddCategory.getText().trim();

            if (name.isEmpty() || category.isEmpty() || price <= 0 || qty < 0) {
                showAlert("Invalid Input", "All fields must be filled correctly.");
                return;
            }

            Product newProduct = new Product(name, price, qty, category);
            ProductFileManager.addProduct(newProduct);

            clearAddFields();
            soundManager.playSuccessSound();
            showAlert("Success", "Product added!");

        } catch (Exception ex) {
            showAlert("Error", "Invalid input format.");
        }
    }

    private void clearAddFields() {
        txtAddName.clear();
        txtAddPrice.clear();
        txtAddQty.clear();
        txtAddCategory.clear();
    }

    // Edit Product
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
            return;
        }

        try {
            double newPrice = txtEditPrice.getText().isEmpty()
                    ? target.getPrice()
                    : Double.parseDouble(txtEditPrice.getText());

            int newQty = txtEditQty.getText().isEmpty()
                    ? target.getQuantity()
                    : Integer.parseInt(txtEditQty.getText());

            String newCategory = txtEditCategory.getText().isEmpty()
                    ? target.getCategory()
                    : txtEditCategory.getText();

            target.setPrice(newPrice);
            target.setQuantity(newQty);
            target.setCategory(newCategory);

            ProductFileManager.saveProducts();
            tblProducts.refresh();
            clearEditFields();

            soundManager.playSuccessSound();
            showAlert("Updated", "Product updated!");

        } catch (Exception ex) {
            showAlert("Error", "Invalid input.");
        }
    }

    private void clearEditFields() {
        txtEditName.clear();
        txtEditPrice.clear();
        txtEditQty.clear();
        txtEditCategory.clear();
    }

    // Remove Product
    @FXML
    private void handleRemoveProduct(ActionEvent e) {

        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can remove products.");
            return;
        }

        String name = txtRemoveName.getText().trim();
        Product target = findProductByName(name);

        if (target == null) {
            showAlert("Error", "Product not found.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + name + "?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            ProductFileManager.removeProduct(target);
            tblProducts.refresh();
            txtRemoveName.clear();
            showAlert("Deleted", "Product removed.");
        }
    }

    @FXML
    private void handleClearInventory(ActionEvent e) {

        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can clear the inventory.");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to remove ALL products from inventory?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirm.setHeaderText(null);
        confirm.setTitle("Confirm Inventory Clear");

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            products.clear();
            ProductFileManager.saveProducts();
            tblProducts.refresh();

            showAlert("Inventory Cleared", "All products have been removed.");
        }
    }


    // Cart Management
    @FXML
    private void handleAddToCart(ActionEvent e) {

        String name = txtCartName.getText().trim();

        Product selected = findProductByName(name);
        if (selected == null) {
            showAlert("Not Found", "Product does not exist.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(txtCartQty.getText());
        } catch (Exception ex) {
            showAlert("Error", "Invalid quantity.");
            return;
        }

        if (selected.getQuantity() < qty) {
            showAlert("Error", "Not enough stock.");
            return;
        }

        selected.setQuantity(selected.getQuantity() - qty);
        cartItems.add(new CartItem(name, qty, selected.getPrice() * qty));

        updateCartTotal();
        ProductFileManager.saveProducts();
        tblProducts.refresh();
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent e) {
        CartItem selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartItems.remove(selected);
            updateCartTotal();
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

        showAlert("Success", "Purchase complete!");
    }

    @FXML
    private void handleViewReport(ActionEvent e) {
        try {
            Stage stage = new Stage();
            new org.minimarket.client.salesReport.SalesReportView().start(stage);
        } catch (Exception ex) {
            showAlert("Error", "Could not open Sales Report window.");
            ex.printStackTrace();
        }
    }


    // Helpers
    private void updateCartTotal() {
        double total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        lblCartTotal.setText(String.format("Cart Total: £%.2f", total));
    }

    private Product findProductByName(String name) {
        for (Product p : products)
            if (p.getName().equalsIgnoreCase(name))
                return p;
        return null;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleRefreshSales(ActionEvent e) {
        totalSales = salesFileManager.loadTotalSales();
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        showAlert("Sales Updated", "Sales refreshed.");
    }
}

