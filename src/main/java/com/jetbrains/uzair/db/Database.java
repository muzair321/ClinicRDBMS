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
                """};
        try(Connection conn = getConnection()){
            Statement stmt = conn.createStatement();
            for(String sql: sqls) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Error Creating Tables: " + e.getMessage());
        }
    }
}