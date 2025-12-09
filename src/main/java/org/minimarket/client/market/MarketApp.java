package org.minimarket.client.market;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.minimarket.utility.SoundManager;




/**
 * Launches the Buyer or Worker view depending on the provided role.
 */
public class MarketApp extends Application {

    private static String userRole;

    public MarketApp(String role) {
        userRole = role;
    }

    public MarketApp() {}

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        Parent root;

        // Create shared SoundManager instance
        SoundManager soundManager = new SoundManager();

        if ("worker".equalsIgnoreCase(userRole)) {
            loader = new FXMLLoader(getClass().getResource("/market.fxml"));
            root = loader.load();

            MarketController controller = loader.getController();
            controller.setUserRole("worker");
            controller.setSoundManager(soundManager);   // <-- ADD THIS

            stage.setTitle("Mini Market - Worker Dashboard");

        } else {
            loader = new FXMLLoader(getClass().getResource("/market_buyer.fxml"));
            root = loader.load();

            BuyerController controller = loader.getController();
            controller.setSoundManager(soundManager);   // <-- ADD THIS

            stage.setTitle("Mini Market - Buyer View");
        }

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}
