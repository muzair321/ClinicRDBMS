package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.File;

public class Database {
//    public static void main(String[] args){
//        createTables();
//    }
    private static final String DB_PATH =
            System.getProperty("user.home") + "/ClinicApp/clinic.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;


    public static Connection getConnection() throws SQLException {
        ensureDbDirectory();
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
                age INTEGER CHECK (age >= 0),
                gender TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS visits(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER,
                paid INTEGER DEFAULT 0,
                treatment TEXT,
                visit_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL
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
            """,
                """
            CREATE TABLE IF NOT EXISTS visit_inventory(
                visit_id INTEGER NOT NULL,
                inventory_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL CHECK (quantity > 0),
                PRIMARY KEY (visit_id, inventory_id),
                FOREIGN KEY (visit_id) REFERENCES visits(id) ON DELETE CASCADE,
                FOREIGN KEY (inventory_id) REFERENCES inventory(id)
            );
            """,
            """
            CREATE TRIGGER IF NOT EXISTS trg_inventory_update
            AFTER UPDATE ON inventory
            BEGIN
                UPDATE inventory
                SET updated_at = CURRENT_TIMESTAMP
                WHERE id = NEW.id;
            END;
            """
        };
        String[] indexes = {
                "CREATE INDEX IF NOT EXISTS idx_patients_phone ON patients(phone)",
                "CREATE INDEX IF NOT EXISTS idx_visits_patient_id ON visits(patient_id)",
                "CREATE INDEX IF NOT EXISTS idx_inventory_name ON inventory(name)",
                "CREATE INDEX IF NOT EXISTS idx_vi_visit ON visit_inventory(visit_id)",
                "CREATE INDEX IF NOT EXISTS idx_vi_inventory ON visit_inventory(inventory_id)"
        };
        try(Connection conn = getConnection()){
            Statement stmt = conn.createStatement();
            for(String sql: sqls){
                stmt.execute(sql);
            }
            for(String sql: indexes){
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception While Creating Table", e);
        }
    }
    public static void backupDatabase(String backupPath) throws SQLException {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("BACKUP TO '" + backupPath + "'");
            }
        }
    }
    private static void ensureDbDirectory() {
        File dir = new File(System.getProperty("user.home"), "ClinicApp");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
