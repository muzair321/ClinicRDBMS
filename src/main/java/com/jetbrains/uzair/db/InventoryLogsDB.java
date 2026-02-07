package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.InventoryLogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
