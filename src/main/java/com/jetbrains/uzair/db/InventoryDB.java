package com.jetbrains.uzair.db;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.jetbrains.uzair.model.Inventory;

public class InventoryDB{
    public static void addInventory(Inventory i){
        String sql = "INSERT INTO inventory(name, dosage, amount, unit_price) VALUES(?, ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, i.getName());
            stmt.setString(2, i.getDosage());
            stmt.setInt(3, i.getAmount());
            stmt.setDouble(4, i.getUnitPrice());
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}