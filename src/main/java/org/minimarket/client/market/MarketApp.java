package org.minimarket.client.market;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the Buyer or Worker view depending on the provided role.
 */
public class MarketApp extends Application {

    private static String userRole;

    // Constructor used when launching from RoleSelectionController
    public MarketApp(String role) {
        userRole = role;
    }

    // Default constructor required by JavaFX
    public MarketApp() {}

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        Parent root;

        if ("worker".equalsIgnoreCase(userRole)) {
            loader = new FXMLLoader(getClass().getResource("/market.fxml"));
            root = loader.load();

            MarketController controller = loader.getController();
            controller.setUserRole("worker");

            stage.setTitle("Mini Market - Worker Dashboard");
        } else {
            loader = new FXMLLoader(getClass().getResource("/market_buyer.fxml"));
            root = loader.load();

            stage.setTitle("Mini Market - Buyer View");
        }

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
