package org.minimarket.auth;

/**
 * Represents a user in the Mini Market system.
 * A user has a username, password, and role which determines
 * what part of the system they can access (e.g., buyer or worker).
 */
public class User {

    // The unique username used to log in
    private String username;

    // The password associated with the user account
    private String password;

    // The role of the user (e.g., "buyer", "worker")
    private String role;

    /**
     * Constructs a new User object with the given credentials and role.
     *
     * @param username The username for login authentication
     * @param password The password for login authentication
     * @param role     The system access level (buyer or worker)
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the username of the user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     * Note: passwords are stored in plain text for simplicity in this coursework.
     * In real applications, passwords must be encrypted.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the role of the user (buyer or worker).
     * This determines which dashboard the system loads after login.
     *
     * @return the user's role
     */
    public String getRole() {
        return role;
    }
}

