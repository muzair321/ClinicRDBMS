package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

public class Database {

    // For Getting Connection, Initializing Database and Creating Tables
    public static Connection getConnection(){
        String userHome = System.getProperty("user.home");
        String URL = "jdbc:sqlite:" + userHome + "/clinicData.db";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        } catch (SQLException e) {
            System.err.println("Error Connecting To Database: " + e.getMessage());
        }
        return  conn;
    }
    //initial tables
    public static void createTables(){
        String[] sqls = {"""
                CREATE TABLE IF NOT EXISTS patients(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL CHECK (age >= 0),
                gender TEXT NOT NULL CHECK ( gender IN ('Male', 'Female', 'Other')),
                phone TEXT UNIQUE CHECK (phone IS NULL OR length(phone) >= 7),
                created_at TEXT NOT NULL DEFAULT (datetime('now'))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS visits(
                id INTEGER  PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                illness TEXT,
                treatment TEXT,
                paid INTEGER NOT NULL CHECK (paid >= 0),
                date TEXT NOT NULL DEFAULT (datetime('now')),
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
                storage TEXT NOT NULL CHECK (storage IN ('Bottles', 'Strips', 'Tablets', 'Tubes', 'Powder Pack')),
                amount INTEGER NOT NULL CHECK (amount >= 0),
                updated_at TEXT NOT NULL DEFAULT (datetime('now'))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS inventory_logs(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                inventory_id INTEGER NOT NULL,
                date TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (inventory_id)
                    REFERENCES inventory(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                admin INTEGER NOT NULL CHECK (admin IN (1, 0))
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS user_logs(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                date TEXT NOT NULL DEFAULT (datetime ('now')),
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
            System.err.println("Error Creating Tables: " + e.getMessage());
        }
    }
    public static void addIndexes() throws SQLException{
        String[] sql = {
                """
                
                """,
                """
                
                """,
                """
                
                """,
                """
                
                """
            };
        try(Connection conn = getConnection();
        Statement stmt = conn.createStatement()){
            stmt.execute();
        } catch (SQLException e) {
            throw new SQLException("Error Creating Triggers");
        }
    }
    public static void addTriggers() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TRIGGER IF NOT EXISTS inventory_log_updates_inventory
            AFTER INSERT ON inventory_logs
            FOR EACH ROW
            BEGIN
                UPDATE inventory
                SET updated_at = datetime('now')
                WHERE id = NEW.inventory_id;
            END;
        """);

        } catch (SQLException e) {
            throw new SQLException("Error adding inventory triggers", e);
        }
    }

    public static void backup(String location){
    }
}