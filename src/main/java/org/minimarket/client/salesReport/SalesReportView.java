package org.minimarket.client.salesReport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SalesReportView extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sales_report.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Sales Report");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠ Failed to load Sales Report window.");
        }
    }

    public void open(Stage stage) {
        start(stage);
    }
}
