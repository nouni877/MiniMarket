package org.minimarket.auth;

/**
 * Represents a user in the Mini Market system.
 * A user has a username, password, and role which determines
 * what part of the system they can access (e.g., buyer or worker).
 */
public class User {

    private String username;

    private String password;

    private String role;

    /**
     * @param username The username for login authentication
     * @param password The password for login authentication
     * @param role     The system access level
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

