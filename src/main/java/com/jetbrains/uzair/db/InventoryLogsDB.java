package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.InventoryLogs;
import com.jetbrains.uzair.model.Visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryLogsDB {
    public static void insert(InventoryLogs il) throws SQLException {
        String sql = "INSERT INTO inventory_logs( id, inventory_id, user_id, amount, date) VALUES(?, ?, ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, il.getId());
            stmt.setInt(2, il.getInventoryId());
            stmt.setInt(3, il.getUserId());
            stmt.setInt(4, il.getAmount());
            stmt.setString(5, il.getDate());

        } catch (SQLException e) {
            throw new SQLException("Error Inserting Into Inventory Logs");
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
}
