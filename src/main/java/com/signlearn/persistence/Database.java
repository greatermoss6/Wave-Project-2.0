package com.signlearn.persistence;

import com.signlearn.exceptions.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utility that always returns a *new* SQLite connection.
 * Prevents "database busy" issues in multi-threaded contexts.
 */
public class Database {
    // ✅ Use system property override for test database
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:signlearn.db";

    private Database() {
        // utility class, no instantiation
    }

    /**
     * Opens a new connection to the SQLite database with foreign keys enabled.
     */
    public static Connection getConnection() {
        try {
            // if system property db.url is set (e.g. in tests), use it
            String dbUrl = System.getProperty("db.url", DEFAULT_DB_URL);
            Connection conn = DriverManager.getConnection(dbUrl);
            enableForeignKeys(conn);
            return conn;
        } catch (SQLException e) {
            throw new PersistenceException("Failed to open SQLite connection", e);
        }
    }

    private static void enableForeignKeys(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("PRAGMA journal_mode = WAL");    // good for concurrency
            stmt.execute("PRAGMA synchronous = NORMAL");  // balance perf + safety
        }
    }
}