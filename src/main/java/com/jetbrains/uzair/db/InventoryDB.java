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
                    returnSet[i][4] = rs.getString("updated_at");
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
    public static void edit(Inventory i) throws SQLException{
        String sql = "UPDATE inventory SET name =  ?, storage = ?, amount = ?, updated_at = datetime('now') WHERE id = ?";
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(4, i.getId());
            stmt.setString(1, i.getName());
            stmt.setString(2, i.getStorage());
            stmt.setInt(3, i.getAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Editing Inventory Item");
        }
    }
    public static int getStock(int id) throws SQLException{
        String sql = "SELECT amount FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("amount");
        } catch (SQLException e) {
            throw new SQLException("Error Getting Stock Of Item");
        }
    }
    public static void setStock(int inventoryId, int amount) throws SQLException{
        int stock = getStock(inventoryId);
        if(amount < 0){
            if(stock + amount < 0){
                throw new SQLException("Amount Cannot Decrease Stock Beyond Zero");
            }
        }
        String sql = "UPDATE inventory SET amount = ? WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(2, inventoryId);
            stmt.setInt(1, (stock - amount));
        } catch (SQLException e) {
            throw new SQLException("Error Updating Inventory Stock");
        }
    }
}