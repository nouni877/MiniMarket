package org.minimarket.client.auth;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.minimarket.client.market.MarketApp;

/**
 * Controller for handling role selection and authentication in the Mini Market system.
 * Buyers go straight to the buyer interface (product cards view),
 * while workers must log in with a PIN to access the management interface.
 */
public class RoleSelectionController {

    @FXML private HBox roleButtonsBox;
    @FXML private VBox workerLoginBox;
    @FXML private PasswordField txtPin;
    @FXML private Label lblPinMessage;

    // Hardcoded worker PIN (you can later store this securely)
    private static final String WORKER_PIN = "1234";

    /**
     * Handles when the Buyer button is clicked.
     * Opens the Buyer view (market_buyer.fxml) with product cards.
     */
    @FXML
    private void handleBuyer(ActionEvent event) {
        try {
            Stage stage = new Stage();

            // Launch MarketApp in Buyer mode
            MarketApp app = new MarketApp("buyer");
            app.start(stage);

            // Close current Role Selection window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening Buyer view: " + e.getMessage());
        }
    }

    /**
     * Handles when the Worker button is clicked.
     * Displays the PIN login box.
     */
    @FXML
    private void handleWorker() {
        roleButtonsBox.setVisible(false);
        workerLoginBox.setVisible(true);
        txtPin.clear();
        lblPinMessage.setText("");
    }

    /**
     * Handles when the Cancel button is clicked in the worker login box.
     * Returns to the role selection screen.
     */
    @FXML
    private void handleCancelWorkerLogin() {
        workerLoginBox.setVisible(false);
        roleButtonsBox.setVisible(true);
        txtPin.clear();
        lblPinMessage.setText("");
    }

    /**
     * Handles when the worker enters the PIN and clicks Login.
     * If the PIN is correct, opens the worker dashboard (market.fxml).
     */
    @FXML
    private void handleWorkerLogin(ActionEvent event) {
        String enteredPin = txtPin.getText().trim();

        if (enteredPin.isEmpty()) {
            lblPinMessage.setText("Please enter your PIN.");
            return;
        }

        if (enteredPin.equals(WORKER_PIN)) {
            try {
                Stage stage = new Stage();

                // Launch MarketApp in Worker mode
                MarketApp app = new MarketApp("worker");
                app.start(stage);

                // Close current login window
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error opening Worker view: " + e.getMessage());
            }
        } else {
            lblPinMessage.setText("Incorrect PIN. Please try again.");
        }
    }

    /**
     * Displays a small alert box for errors.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

