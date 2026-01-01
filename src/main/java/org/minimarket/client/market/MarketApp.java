package org.minimarket.client.market;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.minimarket.utility.SoundManager;


/**
 * MarketApp is the main JavaFX entry point for the Mini Market system.
 * It loads different interfaces depending on the user's role
 * (worker or buyer) and injects shared resources such as SoundManager.
 */
public class MarketApp extends Application {

    // Stores the role of the current user (buyer or worker)
    private static String userRole;

    /**
     * Constructor used when launching the application with a specified role.
     */
    public MarketApp(String role) {
        userRole = role;
    }

    /**
     * Default constructor required by JavaFX.
     */
    public MarketApp() {}

    /**
     * Starts the JavaFX application.
     * Loads the appropriate FXML file based on user role
     * and configures the controller accordingly.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        Parent root;

        // Create a shared SoundManager instance
        // This allows consistent sound effects across different views
        SoundManager soundManager = new SoundManager();

        // Load worker interface if user role is "worker"
        if ("worker".equalsIgnoreCase(userRole)) {

            loader = new FXMLLoader(getClass().getResource("/market.fxml"));
            root = loader.load();

            // Configure worker controller
            MarketController controller = loader.getController();
            controller.setUserRole("worker");
            controller.setSoundManager(soundManager);

            stage.setTitle("Mini Market - Worker Dashboard");

        }
        // Otherwise load buyer interface
        else {

            loader = new FXMLLoader(getClass().getResource("/market_buyer.fxml"));
            root = loader.load();

            // Configure buyer controller
            BuyerController controller = loader.getController();
            controller.setSoundManager(soundManager);

            stage.setTitle("Mini Market - Buyer View");
        }

        // Create and display the main application window
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
