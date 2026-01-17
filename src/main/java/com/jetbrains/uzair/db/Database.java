package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
    public static void main(String[] args){
        createTables();
    }
    private static final String URL = "jdbc:sqlite:clinic.db";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("foreign_keys", "true");
        props.setProperty("date_string_format", "yyyy-MM-dd HH:mm:ss");

        Connection conn = DriverManager.getConnection(URL, props);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }
    public static void createTables(){
        String[] sqls = {"""
            CREATE TABLE IF NOT EXISTS patients(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                phone TEXT UNIQUE NOT NULL,
                name TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS visits(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                inventory_id INTEGER,
                patient_id INTEGER,
                name TEXT NOT NULL,
                gender TEXT,
                age INTEGER CHECK (age >= 0),
                paid INTEGER DEFAULT 0,
                phone TEXT,
                treatment TEXT,
                visit_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL,
                FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE SET NULL
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS inventory(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                dosage TEXT,
                amount INTEGER DEFAULT 0 CHECK (amount >= 0),
                expy TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """};
        try(Connection conn = getConnection()){
            Statement stmt = conn.createStatement();
            for(String sql: sqls){
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception While Creating Table", e);
        }
    }
}
