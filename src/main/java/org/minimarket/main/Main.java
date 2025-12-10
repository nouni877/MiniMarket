package org.minimarket.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.minimarket.client.market.MarketController;
import org.minimarket.client.market.BuyerController;

/**
 * The entry point of the Mini Market application.
 * This class manages the main JavaFX stage and handles navigation between different pages (Login, Buyer, Worker).
 *
 * The primaryStage is stored as a static variable so that other controllers can easily request page transitions using static methods.
 */
public class Main extends Application {

    // The main window used throughout the application
    private static Stage primaryStage;

    /**
     * JavaFX lifecycle method called when the application starts.
     * Initializes the primary stage and loads the login screen.
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;   // Store the stage for global access
        loadLoginPage();        // Display the login page first
    }

    /**
     * Loads the Login page.
     * This is the default starting page for users.
     */
    public static void loadLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/login.fxml"));
        Parent root = loader.load();

        // Set scene size to match other pages for consistency
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Mini Market - Login");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Loads the Worker dashboard page.
     * This page allows staff to manage products and sales.
     */
    public static void loadWorkerPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/market.fxml"));
        Parent root = loader.load();

        // Inject worker role into the controller
        MarketController controller = loader.getController();
        controller.setUserRole("worker");

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Mini Market - Worker Dashboard");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Loads the Buyer page.
     * This page allows customers to browse items and make purchases.
     */
    public static void loadBuyerPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/market_buyer.fxml"));
        Parent root = loader.load();

        // Controller can receive additional configuration if needed
        BuyerController controller = loader.getController();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Mini Market - Buyer View");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Main method — launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
