package org.minimarket.client.market;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MarketApp extends Application {
    private static String userRole;

    public MarketApp() {}

    public MarketApp(String role) {
        userRole = role;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/market.fxml"));
        Parent root = loader.load();

        // Apply CSS stylesheet properly
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Mini Market Dashboard");
        stage.setScene(scene);
        stage.show();

        // Pass the role to the controller
        MarketController controller = loader.getController();
        controller.setUserRole(userRole);
    }
}
