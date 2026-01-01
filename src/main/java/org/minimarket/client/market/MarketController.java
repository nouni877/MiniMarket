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

/**
 * MarketController handles both buyer and worker interactions.
 * It manages product inventory, shopping cart operations,
 */
public class MarketController {

    //  Product table (inventory view)
    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    //  Search & filter controls
    @FXML private TextField txtSearch;
    @FXML private CheckBox chkLowStock;

    //  Add product fields
    @FXML private TextField txtAddName;
    @FXML private TextField txtAddPrice;
    @FXML private TextField txtAddQty;
    @FXML private TextField txtAddCategory;

    // Edit product fields
    @FXML private TextField txtEditName;
    @FXML private TextField txtEditPrice;
    @FXML private TextField txtEditQty;
    @FXML private TextField txtEditCategory;

    // Remove product field (worker only)
    @FXML private TextField txtRemoveName;

    // Sales & cart UI
    @FXML private Label lblTotalSales;
    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> cartName;
    @FXML private TableColumn<CartItem, Integer> cartQty;
    @FXML private TableColumn<CartItem, Double> cartSubtotal;

    @FXML private TextField txtCartName;
    @FXML private TextField txtCartQty;
    @FXML private Label lblCartTotal;

    // === Worker control buttons ===
    @FXML private Button btnAddProduct;
    @FXML private Button btnEditProduct;
    @FXML private Button btnRemoveProduct;
    @FXML private Button btnClearInventory;

    //  Data collections
    private ObservableList<Product> products;
    private ObservableList<CartItem> cartItems;

    //  Utilities
    private SoundManager soundManager;
    private final SalesFileManager salesFileManager = new SalesFileManager();

    // Sales tracking
    private double totalSales = 0.0;

    // User role (buyer by default)
    private String userRole = "buyer";

    /**
     * Initialises the controller.
     * Sets up table columns, loads products,
     * initialises cart and loads total sales.
     */
    @FXML
    public void initialize() {

        // Bind product table columns to Product properties
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Bind cart table columns to CartItem properties
        cartName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Load products from persistent storage
        ProductFileManager.loadProducts();
        products = ProductFileManager.getProducts();
        tblProducts.setItems(products);

        // Initialise cart
        cartItems = FXCollections.observableArrayList();
        tblCart.setItems(cartItems);

        // Load total sales from file
        totalSales = salesFileManager.loadTotalSales();
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        lblCartTotal.setText("Cart Total: £0.00");
    }

    /**
     * Injects the SoundManager for feedback sounds.
     */
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Configures UI access based on user role.
     * Buyers cannot manage inventory.
     */
    public void setUserRole(String role) {
        this.userRole = role;
        boolean isBuyer = role.equalsIgnoreCase("buyer");

        // Disable inventory controls for buyers
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

    // Helper method to check worker role
    private boolean isWorker() {
        return userRole.equalsIgnoreCase("worker");
    }

    /**
     * Filters products by search keyword.
     */
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

    /**
     * Clears search filter.
     */
    @FXML
    private void handleClearSearch(ActionEvent e) {
        txtSearch.clear();
        tblProducts.setItems(products);
    }

    /**
     * Filters products with low stock levels.
     */
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

    /**
     * Adds a new product to inventory (worker only).
     */
    @FXML
    private void handleAddProduct(ActionEvent e) {
        if (!isWorker()) {
            showAlert("Access Denied", "Only workers can add products.");
            return;
        }

        try {
            String name = txtAddName.getText().trim();
            String category = txtAddCategory.getText().trim();

            double price;
            int qty;

            try {
                price = Double.parseDouble(txtAddPrice.getText());
                qty = Integer.parseInt(txtAddQty.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Price and quantity must be valid numbers.");
                return;
            }

            if (name.isEmpty() || category.isEmpty() || price <= 0 || qty < 0) {
                showAlert("Invalid Input", "All fields must be filled correctly.");
                return;
            }

            Product newProduct = new Product(name, price, qty, category);
            ProductFileManager.addProduct(newProduct);

            clearAddFields();
            showAlert("Success", "Product added!");

            try {
                soundManager.playSuccessSound();
            } catch (Exception soundEx) {
                System.err.println("Sound could not be played.");
            }

        } catch (Exception ex) {
            showAlert("Error", "An unexpected error occurred while adding the product.");
        }
    }

    // Clears add-product form fields
    private void clearAddFields() {
        txtAddName.clear();
        txtAddPrice.clear();
        txtAddQty.clear();
        txtAddCategory.clear();
    }

    /**
     * Adds selected product to cart and updates stock.
     */
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

    /**
     * Completes checkout and updates total sales.
     */
    @FXML
    private void handleCheckout(ActionEvent e) {

        if (cartItems.isEmpty()) {
            showAlert("Cart Empty", "Add items before checkout.");
            return;
        }

        double cartTotal = 0.0;

        // 1) Save each cart item as a sale record (so it appears in report)
        for (CartItem item : cartItems) {
            cartTotal += item.getSubtotal();

            salesFileManager.saveSaleRecords(
                    java.util.List.of(
                            new org.minimarket.catalogue.SaleRecord(
                                    item.getProductName(),
                                    item.getQuantity(),
                                    item.getSubtotal()
                            )
                    )
            );
        }

        // 2) Update total sales (stored total)
        totalSales += cartTotal;
        salesFileManager.saveTotalSales(totalSales);
        lblTotalSales.setText(String.format("£%.2f", totalSales));

        // 3) Clear cart
        cartItems.clear();
        updateCartTotal();

        showAlert("Success", "Purchase complete!");
    }

    /**
     * Opens sales report window.
     */
    @FXML
    private void handleViewReport(ActionEvent e) {
        try {
            Stage stage = new Stage();
            new org.minimarket.client.salesReport.SalesReportView().start(stage);
        } catch (Exception ex) {
            showAlert("Error", "Could not open Sales Report window.");
        }
    }

    // Updates cart total label
    private void updateCartTotal() {
        double total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        lblCartTotal.setText(String.format("Cart Total: £%.2f", total));
    }

    // Finds product by name (case-insensitive)
    private Product findProductByName(String name) {
        for (Product p : products)
            if (p.getName().equalsIgnoreCase(name))
                return p;
        return null;
    }

    // Displays information alerts
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Reloads total sales from file.
     */
    @FXML
    private void handleRefreshSales(ActionEvent e) {
        totalSales = salesFileManager.loadTotalSales();
        lblTotalSales.setText(String.format("£%.2f", totalSales));
        showAlert("Sales Updated", "Sales refreshed.");
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
    private void handleEditProduct(ActionEvent e) {
    }

    @FXML
    private void handleRemoveProduct(ActionEvent e) {
    }

    @FXML
    private void handleClearInventory(ActionEvent e) {
    }


}

