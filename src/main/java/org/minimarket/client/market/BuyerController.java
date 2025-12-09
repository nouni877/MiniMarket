package org.minimarket.client.market;

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
import org.minimarket.catalogue.Product;
import org.minimarket.main.Main;
import org.minimarket.storageAccess.ProductFileManager;
import org.minimarket.storageAccess.SalesFileManager;
import java.util.List;
import java.util.ArrayList;
import org.minimarket.catalogue.SaleRecord;
import org.minimarket.utility.SoundManager;



public class BuyerController {

    @FXML private FlowPane productContainer;
    @FXML private ListView<String> cartList;
    @FXML private Label cartTotalLabel;
    @FXML private ComboBox<String> categoryFilter; // NEW
    @FXML private TextField txtSearch;


    private double total = 0.0;
    private final SalesFileManager salesFileManager = new SalesFileManager();

    @FXML
    public void initialize() {
        ProductFileManager.loadProducts();

        setupCategoryFilter();

        refreshProductDisplay();

        ProductFileManager.getProducts().addListener((ListChangeListener<Product>) change -> {
            refreshProductDisplay();
        });
    }

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

    /**
     * Refresh product cards whenever data updates.
     * Now includes category filtering.
     */
    private void refreshProductDisplay() {
        productContainer.getChildren().clear();

        String selectedCategory = categoryFilter.getValue();

        for (Product p : ProductFileManager.getProducts()) {
            if (selectedCategory.equals("All") || p.getCategory().equals(selectedCategory)) {
                addProductCard(p);
            }
        }
    }

    /**
     * Creates and adds a product card for each item.
     */
    private void addProductCard(Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.08);"
                + "-fx-background-radius: 15;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
                + "-fx-pref-width: 180;");
        String imagePath = "/images/" + product.getName().toLowerCase() + ".png";
        ImageView imageView;
        try {
            imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/default.png")));
        }

        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

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

    /**
     * Adds selected product to cart.
     */
    private void addToCart(Product product) {
        if (product.getQuantity() <= 0) {
            showAlert("Out of Stock", product.getName() + " is currently out of stock.");
            return;
        }

        product.setQuantity(product.getQuantity() - 1);
        ProductFileManager.saveProducts();

        // Add to buyer's cart
        cartList.getItems().add(product.getName() + " - £" + String.format("%.2f", product.getPrice()));
        total += product.getPrice();
        cartTotalLabel.setText(String.format("£%.2f", total));

        refreshProductDisplay(); // update stock display
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (cartList.getItems().isEmpty()) {
            showAlert("Cart Empty", "Please add some products to the cart first.");
            return;
        }

        double currentSales = salesFileManager.loadTotalSales();
        double updatedSales = currentSales + total;
        salesFileManager.saveTotalSales(updatedSales);

        List<SaleRecord> records = new ArrayList<>();
        for (String item : cartList.getItems()) {
            String[] parts = item.split(" - £");
            if (parts.length == 2) {
                String name = parts[0].trim();
                double subtotal = Double.parseDouble(parts[1].trim());
                records.add(new SaleRecord(name, 1, subtotal));
            }
        }

        salesFileManager.saveSaleRecords(records);
        ProductFileManager.saveProducts();

        cartList.getItems().clear();
        total = 0.0;
        cartTotalLabel.setText("£0.00");

        showAlert("Purchase Complete", "Thank you for your purchase!\nYour order has been recorded.");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            new Main().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim().toLowerCase();

        productContainer.getChildren().clear();

        String selectedCategory = categoryFilter.getValue();

        for (Product p : ProductFileManager.getProducts()) {
            boolean matchesName = p.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory.equals("All") || p.getCategory().equals(selectedCategory);

            if (matchesName && matchesCategory) {
                addProductCard(p);
            }
        }
    }

    @FXML
    private void handleClearSearch(ActionEvent event) {
        txtSearch.clear();
        refreshProductDisplay();
    }
    private SoundManager soundManager;

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }



}
