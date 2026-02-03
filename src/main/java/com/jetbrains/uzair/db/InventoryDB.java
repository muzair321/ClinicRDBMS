package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Inventory;

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
throw new SQLException("Error Inserting Data Into 'inventory': " + e.getMessage);
}
}
}