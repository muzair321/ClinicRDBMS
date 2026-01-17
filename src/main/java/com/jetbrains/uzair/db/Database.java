package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
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
                phone TEXT UNIQUE
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS visits(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                gender TEXT,
                age INTEGER,
                date TEXT,
                inventory_id INTEGER,
                paid INTEGER,
                phone TEXT,
                patient_id INTEGER,
                treatment TEXT
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS inventory(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                amount INTEGER,
                age INTEGER,
                expy TEXT,
                dosage TEXT
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
