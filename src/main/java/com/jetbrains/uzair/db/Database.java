package com.jetbrains.uzair.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

public class Database {

    // For Getting Connection, Initializing Database and Creating Tables
    public static Connection getConnection() throws SQLException{
        Connection conn = null;
        String userHome;
        try {
            userHome = FolderCreation.createDirectories();
        } catch (IOException e) {
            throw new SQLException("Error Creating Folder For Save " + e.getMessage());
        }
        String URL = "jdbc:sqlite:" + userHome + "/clinicData.db";
        try{
            conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        } catch (SQLException e) {
            throw new SQLException("Error Connecting To Database: " + e.getMessage());
        }
        return  conn;
    }
    //Initial Tables
    public static void createTables() throws SQLException{
        String[] sqls = {"""
                CREATE TABLE IF NOT EXISTS patients(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL CHECK (age >= 0),
                gender TEXT NOT NULL CHECK ( gender IN ('Male', 'Female', 'Other')),
                phone TEXT UNIQUE CHECK (phone IS NULL OR length(phone) >= 7),
                created_at TEXT NOT NULL DEFAULT (datetime('now','localtime'))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                admin INTEGER NOT NULL DEFAULT 0 CHECK (admin IN (0,1))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS visits(
                id INTEGER  PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                illness TEXT,
                treatment TEXT,
                paid INTEGER NOT NULL CHECK (paid >= 0),
                date TEXT NOT NULL DEFAULT (datetime('now','localtime')),
                FOREIGN KEY (patient_id)
                    REFERENCES patients(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS inventory(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT UNIQUE NOT NULL,
                storage TEXT NOT NULL CHECK (storage IN ('Bottles', 'Strips', 'Tablets', 'Tubes', 'Powder Packs')),
                amount INTEGER NOT NULL CHECK (amount >= 0),
                alert INTEGER NOT NULL CHECK (amount >= 0),
                updated_at TEXT NOT NULL DEFAULT (datetime('now','localtime'))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS inventory_logs(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                inventory_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                amount INTEGER NOT NULL CHECK(amount != 0),
                date TEXT NOT NULL DEFAULT (datetime('now','localtime')),
                FOREIGN KEY (inventory_id)
                    REFERENCES inventory(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,
                FOREIGN KEY (user_id)
                    REFERENCES users(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS user_logs(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                date TEXT NOT NULL DEFAULT (datetime ('now','localtime')),
                FOREIGN KEY (user_id)
                    REFERENCES users(id)
                    ON DELETE CASCADE
                );
                """
                };
        try(Connection conn = getConnection()){
            Statement stmt = conn.createStatement();
            for(String sql: sqls) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new SQLException("Error Creating Tables: " + e.getMessage());
        }
    }
//Creating Indexes
    public static void addIndexes() throws SQLException{
        String[] sqls = {
                """
                CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(name);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_patients_created_at ON patients(created_at);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_visits_illness ON visits(illness);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_visits_date ON visits(date);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_inventory_name ON inventory(name);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_inventory_updated_at ON inventory(updated_at);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_inventory_logs_inventory_id ON inventory_logs(inventory_id);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_inventory_logs_user_id ON inventory_logs(user_id);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_inventory_logs_date ON inventory_logs(date);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
                """,
                """
                CREATE INDEX IF NOT EXISTS idx_user_logs_date ON user_logs(date);
                """
            };
        try(Connection conn = getConnection();
        Statement stmt = conn.createStatement()){
            for(String sql: sqls) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new SQLException("Error Creating Indexes");
        }
    }
//Adding Triggers 
    public static void addTriggers() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TRIGGER IF NOT EXISTS inventory_log_updates_inventory
            AFTER INSERT ON inventory_logs
            FOR EACH ROW
            BEGIN
                UPDATE inventory
                SET updated_at = datetime('now','localtime')
                WHERE id = NEW.inventory_id;
            END;
        """);

        } catch (SQLException e) {
            throw new SQLException("Error Adding Triggers");
        }
    }
}