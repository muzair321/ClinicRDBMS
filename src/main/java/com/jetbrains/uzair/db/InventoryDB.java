package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Inventory;

import java.sql.*;

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
    public static String[][] returnUI() throws SQLException{
        String[][] returnSet;
        int count = 0;
        String sql = "SELECT * FROM inventory";
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                count++;
            }
            returnSet = new String[count][];
            try {
                ResultSet rs1 = stmt.executeQuery(sql);
                for (int i = 0; rs.next() && i < returnSet.length; i++){
                    returnSet[i][0] = rs.getString("id");
                    returnSet[i][1] = rs.getString("name");
                    returnSet[i][2] = rs.getString("storage");
                    returnSet[i][3] = rs.getString("amount");
                    returnSet[i][4] = rs.getString("upated_at");
                }
                return returnSet;
            } catch (SQLException e) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new SQLException("Error Retrieving Data From 'inventory'");
        }
    }
    public static Inventory returnUISingle(int id) throws SQLException{
        Inventory i = new Inventory();
        String sql = "SELECT id, name, storage, amount, updated_at FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                i.setId(rs.getInt(1));
                i.setName(rs.getString(2));
                i.setStorage(rs.getString(3));
                i.setAmount(rs.getInt(4));
                i.setUpdatedAt(rs.getString(5));
            }
            return i;
        } catch (SQLException e) {
            throw new SQLException("Error Getting Item Data From 'inventory");
        }
    }
}