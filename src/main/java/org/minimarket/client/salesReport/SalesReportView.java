package org.minimarket.client.salesReport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SalesReportView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/sales_report.fxml")));
        stage.setTitle("Sales Report");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
