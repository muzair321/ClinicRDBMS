package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.File;

public class Database {
    private static final String DB_PATH =
            System.getProperty("user.home") + "/ClinicApp/clinic.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        ensureDbDirectory();
        Properties props = new Properties();
        props.setProperty("foreign_keys", "true");
        props.setProperty("date_string_format", "yyyy-MM-dd HH:mm:ss");
        Connection conn = DriverManager.getConnection(URL, props);
        try (Statement stmt = conn.createStatement()) {
            // Configure SQLite for better performance
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("PRAGMA journal_mode = WAL");
            stmt.execute("PRAGMA synchronous = NORMAL");
            stmt.execute("PRAGMA cache_size = -2000");
            stmt.execute("PRAGMA busy_timeout = 5000");
        }
        return conn;
    }
    public static void createTables() {
        String[] sqls = {
                """
            CREATE TABLE IF NOT EXISTS patients(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                phone TEXT UNIQUE NOT NULL,
                name TEXT,
                age INTEGER CHECK (age >= 0),
                gender TEXT CHECK(gender IN ('Male', 'Female', 'Other')),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS visits(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                treatment TEXT,
                notes TEXT,
                paid_amount INTEGER DEFAULT 0 CHECK (paid_amount >= 0),
                total_amount INTEGER DEFAULT 0 CHECK (total_amount >= 0),
                visit_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS inventory(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                dosage TEXT,
                amount INTEGER DEFAULT 0 CHECK (amount >= 0),
                unit_price DECIMAL(10, 2) DEFAULT 0.0 CHECK (unit_price >= 0),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS visit_inventory(
                visit_id INTEGER NOT NULL,
                inventory_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL CHECK (quantity > 0),
                price_per_unit DECIMAL(10, 2) NOT NULL CHECK (price_per_unit >= 0),
                PRIMARY KEY (visit_id, inventory_id),
                FOREIGN KEY (visit_id) REFERENCES visits(id) ON DELETE CASCADE,
                FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE RESTRICT
            );
            """,
                """
            CREATE TRIGGER IF NOT EXISTS trg_inventory_updated_at
            AFTER UPDATE ON inventory
            BEGIN
                UPDATE inventory 
                SET updated_at = CURRENT_TIMESTAMP 
                WHERE id = NEW.id;
            END;
            """,
                """
            CREATE TRIGGER IF NOT EXISTS trg_update_visit_total
            AFTER INSERT ON visit_inventory
            BEGIN
                UPDATE visits
                SET total_amount = (
                    SELECT COALESCE(SUM(vi.quantity * vi.price_per_unit), 0)
                    FROM visit_inventory vi
                    WHERE vi.visit_id = NEW.visit_id
                )
                WHERE id = NEW.visit_id;
            END;
            """,
                """
            CREATE TRIGGER IF NOT EXISTS trg_update_visit_total_on_update
            AFTER UPDATE ON visit_inventory
            BEGIN
                UPDATE visits
                SET total_amount = (
                    SELECT COALESCE(SUM(vi.quantity * vi.price_per_unit), 0)
                    FROM visit_inventory vi
                    WHERE vi.visit_id = NEW.visit_id
                )
                WHERE id = NEW.visit_id;
            END;
            """,
                """
            CREATE TRIGGER IF NOT EXISTS trg_update_visit_total_on_delete
            AFTER DELETE ON visit_inventory
            BEGIN
                UPDATE visits
                SET total_amount = (
                    SELECT COALESCE(SUM(vi.quantity * vi.price_per_unit), 0)
                    FROM visit_inventory vi
                    WHERE vi.visit_id = OLD.visit_id
                )
                WHERE id = OLD.visit_id;
            END;
            """
        };
        String[] indexes = {
                "CREATE INDEX IF NOT EXISTS idx_patients_phone ON patients(phone)",
                "CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(name)",
                "CREATE INDEX IF NOT EXISTS idx_visits_patient_id ON visits(patient_id)",
                "CREATE INDEX IF NOT EXISTS idx_visits_date ON visits(visit_date)",
                "CREATE INDEX IF NOT EXISTS idx_inventory_name ON inventory(name)",
                "CREATE INDEX IF NOT EXISTS idx_visit_inventory_visit ON visit_inventory(visit_id)",
                "CREATE INDEX IF NOT EXISTS idx_visit_inventory_inv ON visit_inventory(inventory_id)"
        };
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : sqls) {
                stmt.execute(sql);
            }
            for (String sql : indexes) {
                stmt.execute(sql);
            }
            System.out.println("Database tables created successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Exception while creating database tables", e);
        }
    }
    public static void backupDatabase(String backupPath) throws SQLException {
        ensureDbDirectory();
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("VACCUM INTO'" + backupPath + "'");
            }
        }
    }
    public static void initializeDatabase() {
        System.out.println("Initializing database at: " + DB_PATH);
        ensureDbDirectory();
        createTables();
        System.out.println("Database initialized successfully");
    }
    private static void ensureDbDirectory() {
        File dir = new File(System.getProperty("user.home"), "ClinicApp");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create database directory: " + dir.getAbsolutePath());
        }
    }
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection successful!");
            System.out.println("Database location: " + DB_PATH);

            // Test foreign keys
            try (Statement stmt = conn.createStatement();
                 var rs = stmt.executeQuery("PRAGMA foreign_keys")) {
                if (rs.next()) {
                    System.out.println("Foreign keys: " + (rs.getInt(1) == 1 ? "ENABLED" : "DISABLED"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
            initializeDatabase();
            testConnection();
            System.out.println("Usage:");
            System.out.println("  java Database.java init    - Initialize database");
            System.out.println("  java Database.java backup [path] - Create backup");
    }
}