package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5434/waflya";
    private static final String USER = "postgres";
    private static final String PASSWORD = "waflya";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}