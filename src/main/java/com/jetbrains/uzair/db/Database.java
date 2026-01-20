package com.jetbrains.uzair.db;

import java.sql.*;

public class Database {
    // For Getting Connection, Initializing Database and Creating Tables
    public static Connection getConnection(){
        String userHome = System.getProperty("user.home");
        String URL = "jdbc:sqlite:" + userHome + "/clinicData.db";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL);
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
                gender TEXT NOT NULL CHECK ( gender IN ('Male', 'Female', 'Other'))
                );
                """};
        try(Connection conn = getConnection()){
            Statement stmt = conn.createStatement();
            for(String sql: sqls) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Error Connecting To Database: " + e.getMessage());
        }
    }
}