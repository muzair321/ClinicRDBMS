package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDB{
    public static void insert(Inventory i) throws SQLException {
        String sql = "INSERT INTO inventory(name, storage, amount) VALUES(?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, i.getName());
            stmt.setString(2, i.getStorage());
            stmt.setInt(3, i.getAmount());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            throw new SQLException("Error Inserting Data Into 'inventory': " + e.getMessage());
        }
    }
    public static boolean checkName(String name) throws SQLException{
        String sql = "SELECT name FROM inventory WHERE name = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            throw new SQLException("Error Checking Name In DB");
        }
        return false;
    }
}