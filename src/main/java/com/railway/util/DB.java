
package com.railway.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized JDBC connection utility.
 *
 * Database configuration is read from environment variables:
 * DB_URL, DB_USER and DB_PASSWORD.
 */
public final class DB {

    private static final Logger LOGGER =
            Logger.getLogger(DB.class.getName());

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/railway_reservation"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private DB() {
        // Utility class; do not instantiate.
    }

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is not set: " + name);
        }

        return value.trim();
    }

    /**
     * Returns a new JDBC connection.
     */
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");

        if (url == null || url.isBlank()) {
            url = DEFAULT_URL;
        }

        String username = getRequiredEnv("DB_USER");
        String password = getRequiredEnv("DB_PASSWORD");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,
                    "MySQL JDBC driver was not found.", e);

            throw new SQLException(
                    "MySQL JDBC driver is missing. Check Maven dependencies.",
                    e);
        }

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Checks whether the database connection is working.
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection.isValid(5);
        } catch (SQLException | IllegalStateException e) {
            LOGGER.log(Level.SEVERE,
                    "Database connection test failed.", e);
            return false;
        }
    }
}
