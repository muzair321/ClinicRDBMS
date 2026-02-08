package com.jetbrains.uzair.db;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDB{
    public static void insert(Inventory i) throws SQLException {
        String inventorySql = "INSERT INTO inventory(name, storage, amount) VALUES (?, ?, ?)";
        String logSql = "INSERT INTO inventory_logs(inventory_id, user_id, amount) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            int inventoryId;
            try (PreparedStatement ps = conn.prepareStatement(inventorySql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, i.getName());
                ps.setString(2, i.getStorage());
                ps.setInt(3, i.getAmount());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) {
                    throw new SQLException("Failed to get inventory ID");
                }
                inventoryId = keys.getInt(1);
            }
            try (PreparedStatement ps = conn.prepareStatement(logSql)) {
                ps.setInt(1, inventoryId);
                ps.setInt(2, UserSession.getUserId());
                ps.setInt(3, i.getAmount());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new SQLException("Failed to insert inventory + log" + e);
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
        } catch (SQLException e) {
            throw new SQLException("Error Retrieving Data From 'inventory'");
        }
        returnSet = new String[count][5];
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
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
            stmt.setInt(1, (stock + amount));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Updating Inventory Stock");
        }
    }
    public static String getName(int id) throws SQLException{
        String sql = "SELECT name FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.getString("name");
        } catch (SQLException e) {
            throw new SQLException("Error Retrieving Name From 'inventory'");
        }
    }
    public static String[][] returnUI(String searchTerm) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = """
        SELECT id,
               name,
               storage,
               amount,
               updated_at
        FROM inventory
        WHERE name LIKE ?
            OR storage LIKE ?
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("name"),
                            rs.getString("storage"),
                            String.valueOf(rs.getInt("amount")),
                            rs.getString("updated_at")
                    });
                }
            }
        }
        // Convert List to array
        return rows.toArray(new String[0][]);
    }
    public static void delete(int id) throws SQLException{
        String sql = "DELETE FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Inventory id(" + id + "): " + e.getMessage());
        }
    }
    public static void deleteList(List<Integer> list) throws SQLException{
        String sql = "DELETE FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            for(int id: list){
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }catch (SQLException e){
            throw new SQLException("Error Deleting Inventory List: " + e.getMessage());
        }
    }
    public static boolean exists(int id) throws SQLException{
        String sql = "SELECT name FROM inventory WHERE id = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch (SQLException e){
            throw new SQLException("Error Checking Inventory Existence");
        }
    }
}