package org.minimarket.storageAccess;

import org.minimarket.auth.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading user account information from the users.csv file.
 * This class is responsible for loading all registered users into
 * User objects which are then used by the LoginController to validate
 * login credentials.
 */
public class UserFileManager {

    // Path to the CSV file that stores all usernames, passwords, and roles
    private static final String USER_FILE = "src/main/resources/data/users.csv";

    /**
     * Loads all user records from the users.csv file.
     *
     * The file must contain data in this format:
     * username,password,role
     * buyer1,1234,buyer
     * worker1,1111,worker
     *
     * @return a list of User objects representing all registered users
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {

            String line;
            br.readLine();

            // Read each remaining line and convert it into a User object
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Ensure the CSV row has all required fields
                if (parts.length >= 3) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();

                    users.add(new User(username, password, role));
                }
            }

        } catch (Exception e) {
            // Display error in console if the file cannot be read
            System.err.println("Could not load users.csv: " + e.getMessage());
        }

        return users;
    }
}

