package org.minimarket.client.market;
// JavaFX UI components and layout imports
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.awt.Desktop;


import org.minimarket.catalogue.Product;
import org.minimarket.catalogue.SaleRecord;
import org.minimarket.main.Main;
import org.minimarket.storageAccess.ProductFileManager;
import org.minimarket.storageAccess.SalesFileManager;
import org.minimarket.utility.SoundManager;
import org.minimarket.utility.ReceiptGenerator;


import java.util.ArrayList;
import java.util.List;
/**
 * BuyerController handles all buyer-side functionality,
 * including product display, cart management, searching,
 * checkout processing, and receipt generation.
 */
public class BuyerController {
    // UI elements linked from the FXML file
    @FXML private FlowPane productContainer;
    @FXML private ListView<String> cartList;
    @FXML private Label cartTotalLabel;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private TextField txtSearch;

    private final SalesFileManager salesFileManager = new SalesFileManager();
    private SoundManager soundManager;

    @FXML
    public void initialize() {
        ProductFileManager.loadProducts();
        setupCategoryFilter();
        refreshProductDisplay();
        ProductFileManager.getProducts().addListener((ListChangeListener<Product>) change -> {
            refreshProductDisplay();
        });
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    //  category filter
    private void setupCategoryFilter() {
        categoryFilter.getItems().clear();
        categoryFilter.getItems().add("All");

        for (Product p : ProductFileManager.getProducts()) {
            if (!categoryFilter.getItems().contains(p.getCategory())) {
                categoryFilter.getItems().add(p.getCategory());
            }
        }

        categoryFilter.setValue("All");
        categoryFilter.setOnAction(e -> refreshProductDisplay());
    }

    // refresh products
    private void refreshProductDisplay() {
        productContainer.getChildren().clear();
        String selectedCategory = categoryFilter.getValue();

        for (Product p : ProductFileManager.getProducts()) {
            if (selectedCategory.equals("All") || p.getCategory().equals(selectedCategory)) {
                addProductCard(p);
            }
        }
    }

    // product card UI
    private void addProductCard(Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.08);"
                + "-fx-background-radius: 15;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
                + "-fx-pref-width: 180;");

        ImageView imageView;

        var url = getClass().getResource(product.getImagePath());
        if (url == null) {
            url = getClass().getResource("/images/default.png");
        }

        Image image = new Image(
                url.toExternalForm(),
                120,     //  width
                120,     // height
                true,    // ratio
                true     // smooth
        );

        imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);


        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label priceLabel = new Label(String.format("£%.2f", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 14px;");

        Label stockLabel = new Label("Stock: " + product.getQuantity());
        stockLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");

        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; "
                + "-fx-background-radius: 10; -fx-font-weight: bold; -fx-padding: 5 15;");
        addButton.setOnAction(e -> addToCart(product));

        card.getChildren().addAll(imageView, nameLabel, priceLabel, stockLabel, addButton);
        productContainer.getChildren().add(card);
    }

    // Add to cart
    private void addToCart(Product product) {
        if (product.getQuantity() <= 0) {
            showAlert("Out of Stock", product.getName() + " is currently out of stock.");
            return;
        }

        product.setQuantity(product.getQuantity() - 1);
        ProductFileManager.saveProducts();

        String entry = product.getName() + " - £" + String.format("%.2f", product.getPrice());
        cartList.getItems().add(entry);

        updateCartTotal();
        refreshProductDisplay();
    }

    // Remove
    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        String selected = cartList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartList.getItems().remove(selected);
            updateCartTotal();
        }
    }

    //update cart total
    private void updateCartTotal() {
        double total = 0.0;

        for (String item : cartList.getItems()) {
            try {
                String priceText = item.substring(item.lastIndexOf("£") + 1);
                total += Double.parseDouble(priceText);
            } catch (Exception e) {
                System.err.println("Price parse error: " + item);
            }
        }

        cartTotalLabel.setText(String.format("£%.2f", total));
    }

    @FXML
    private void handleCheckout(ActionEvent event) {

        if (cartList.getItems().isEmpty()) {
            showAlert("Cart Empty", "Please add items before purchasing.");
            return;
        }

        List<SaleRecord> saleRecords = new ArrayList<>();
        double cartTotal = 0.0;

        List<String> rawItems = cartList.getItems();

        for (String item : rawItems) {
            try {
                String name = item.substring(0, item.lastIndexOf(" - £"));
                double price = Double.parseDouble(item.substring(item.lastIndexOf("£") + 1));

                cartTotal += price;
                saleRecords.add(new SaleRecord(name, 1, price));

            } catch (Exception e) {
                System.err.println("Failed to parse cart entry: " + item);
            }
        }

        String receiptPath = ReceiptGenerator.createReceiptFromStrings(rawItems, cartTotal);

        try {
            java.awt.Desktop.getDesktop().open(new File(receiptPath));
        } catch (Exception ex) {
            System.err.println("Could not open receipt file.");
        }


        // Generate Receipt
        ReceiptGenerator.createReceiptFromStrings(rawItems, cartTotal);

        //  Update Total Sales
        double previousSales = salesFileManager.loadTotalSales();
        double updatedSales = previousSales + cartTotal;
        salesFileManager.saveTotalSales(updatedSales);

        salesFileManager.saveSaleRecords(saleRecords);

        // Clear cart UI
        cartList.getItems().clear();
        cartTotalLabel.setText("£0.00");

        showAlert("Success", "Purchase completed! Receipt saved.");
    }
    /**
     * Filters products by search keyword and category.
     */
    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim().toLowerCase();

        productContainer.getChildren().clear();

        String selectedCategory = categoryFilter.getValue();

        for (Product p : ProductFileManager.getProducts()) {
            boolean matchesName = p.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory.equals("All") ||
                    p.getCategory().equals(selectedCategory);

            if (matchesName && matchesCategory) {
                addProductCard(p);
            }
        }
    }
    /**
     * Clears the search field and restores the full product list.
     */
    @FXML
    private void handleClearSearch(ActionEvent event) {
        txtSearch.clear();
        refreshProductDisplay();
    }

    /**
     * Returns the user to the main menu.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            new Main().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
