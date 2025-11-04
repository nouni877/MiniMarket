package org.minimarket.client.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.minimarket.client.market.MarketApp;

public class RoleSelectionController {
    @FXML private HBox roleButtonsBox;
    @FXML private VBox workerLoginBox;
    @FXML private PasswordField txtPin;
    @FXML private Label lblPinMessage;

    private final String WORKER_PIN = "1234";

    @FXML
    private void handleBuyer() {
        try {
            Stage stage = new Stage();
            MarketApp app = new MarketApp("buyer");
            app.start(stage);
            ((Stage) roleButtonsBox.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleWorker() {
        roleButtonsBox.setVisible(false);
        workerLoginBox.setVisible(true);
    }

    @FXML
    private void handleCancelWorkerLogin() {
        workerLoginBox.setVisible(false);
        roleButtonsBox.setVisible(true);
        txtPin.clear();
        lblPinMessage.setText("");
    }

    @FXML
    private void handleWorkerLogin() {
        if (txtPin.getText().equals(WORKER_PIN)) {
            try {
                Stage stage = new Stage();
                MarketApp app = new MarketApp("worker");
                app.start(stage);
                ((Stage) txtPin.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lblPinMessage.setText("❌ Incorrect PIN. Please try again.");
        }
    }
}
