package org.minimarket.client.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.minimarket.auth.User;
import org.minimarket.storageAccess.UserFileManager;
import org.minimarket.main.Main;

import java.util.List;

/**
 * Controller responsible for handling user login functionality.
 * It validates user credentials against data loaded from users.csv
 * and directs the user to the correct dashboard (Buyer or Worker)
 * depending on their assigned role.
 */
public class LoginController {

    // Text field where the user enters their username
    @FXML private TextField usernameField;

    // Password field where the user enters their password
    @FXML private PasswordField passwordField;

    // Label used to show login errors (incorrect credentials, loading issues, etc.)
    @FXML private Label errorLabel;

    // List of all users loaded from the users.csv file
    private List<User> users = UserFileManager.loadUsers();

    /**
     * Called when the user presses the Login button.
     * This method checks the entered username and password against the list of valid users.
     * If a match is found, the system loads the appropriate page depending on the user's role.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Loop through all registered users and compare credentials
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {

                // Successful login → load correct page
                try {
                    if (user.getRole().equalsIgnoreCase("buyer")) {
                        Main.loadBuyerPage();   // Load Buyer Dashboard
                    } else if (user.getRole().equalsIgnoreCase("worker")) {
                        Main.loadWorkerPage();  // Load Worker Dashboard
                    }
                } catch (Exception e) {
                    // If something fails during page loading, show an error to the user
                    errorLabel.setText("Error loading page.");
                }

                return; // Stop once a match is found
            }
        }

        // If no matching user is found, show an error message
        errorLabel.setText("Invalid username or password.");
    }
}

