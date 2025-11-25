package org.minimarket.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.minimarket.client.market.MarketController;
import org.minimarket.client.market.BuyerController;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadRoleSelection();
    }

    /**
     * Loads the role selection screen (default start screen)
     */
    public void loadRoleSelection() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/role_selection.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Mini Market - Role Selection");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Loads the Worker (Market) page and sets role = worker
     */
    public void loadWorkerPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/market.fxml"));
        Parent root = loader.load();

        MarketController controller = loader.getController();
        controller.setUserRole("worker"); //  ensure worker permissions

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Mini Market - Worker Dashboard");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Loads the Buyer page
     */
    public void loadBuyerPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/market_buyer.fxml"));
        Parent root = loader.load();

        BuyerController controller = loader.getController(); // not mandatory, but ready if needed

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Mini Market - Buyer View");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
