package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.InventoryLogs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryLogsDB {
    public static void insert(InventoryLogs il) throws SQLException {
        InventoryDB.setStock(il.getInventoryId(), il.getAmount());
        String sql = "INSERT INTO inventory_logs(inventory_id, user_id, amount) VALUES( ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, il.getInventoryId());
            stmt.setInt(2, il.getUserId());
            stmt.setInt(3, il.getAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Inserting Into Inventory Logs: " +  e.getMessage());
        }
    }
    public static List<InventoryLogs> returnLogs(int inventoryId) throws SQLException{
        List<InventoryLogs> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory_logs WHERE inventory_id = ? ORDER BY date DESC";
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryLogs v = new InventoryLogs();
                v.setId(rs.getInt("id"));
                v.setInventoryId(rs.getInt("inventory_id"));
                v.setUserId(rs.getInt("user_id"));
                v.setAmount(rs.getInt("amount"));
                v.setDate(rs.getString("date"));
                list.add(v);
            }
        }
        return list;
    }
    public static String[][] returnUI() throws SQLException{
        String[][] returnSet;
        int count = 0;
        String sql = """
        SELECT l.id,
               i.name AS item_name,
               u.username AS user_name,
               l.amount,
               l.date
        FROM inventory_logs l
        JOIN users u ON u.id = l.user_id
        JOIN inventory i ON i.id = l.inventory_id
        ORDER BY l.id DESC;
        """;
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                count++;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Retrieving Data From 'inventory_logs'");
        }
        returnSet = new String[count][5];
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            for (int i = 0; rs.next() && i < returnSet.length; i++){
                returnSet[i][0] = rs.getString("id");
                returnSet[i][1] = rs.getString("item_name");
                returnSet[i][2] = rs.getString("user_name");
                returnSet[i][3] = rs.getString("amount");
                returnSet[i][4] = rs.getString("date");
            }
            return returnSet;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
    public static String[][] returnUI(String searchTerm) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = """
        SELECT l.id,
               i.name AS item_name,
               u.username AS user_name,
               l.amount,
               l.date
        FROM inventory_logs l
        JOIN users u ON u.id = l.user_id
        JOIN inventory i ON i.id = l.inventory_id
        WHERE u.username LIKE ?
            OR i.name LIKE ?
            OR l.date LIKE ?
        ORDER BY l.id DESC;
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("item_name"),
                            rs.getString("user_name"),
                            String.valueOf(rs.getInt("amount")),
                            rs.getString("date")
                    });
                }
            }
        }
        // Convert List to array
        return rows.toArray(new String[0][]);
    }
}