package org.minimarket.client.salesReport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SalesReportView is responsible for displaying the Sales Report user interface.
 * It loads the FXML layout and opens a separate window showing all recorded sales.
 *
 * This class represents the **View** in the MVC structure for the sales reporting
 * feature. It does not handle any logic or data processing—its only job is to load
 * the interface and display it to the user.
 */
public class SalesReportView extends Application {

    /**
     * Starts the Sales Report window by loading the FXML file and showing the stage.
     *
     * @param stage the window that will display the sales report
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the Sales Report UI layout from FXML
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/sales_report.fxml"))
        );

        // Configure the window
        stage.setTitle("Sales Report");
        stage.setScene(scene);
        stage.centerOnScreen(); // Center the window on the user's display
        stage.show();           // Make the window visible
    }
}
