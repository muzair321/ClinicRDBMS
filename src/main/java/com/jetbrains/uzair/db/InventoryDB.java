package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Inventory;

public class InventoryDB{
public static void insert(Inventory i) throws SQLException {
String sql = "INSERT INTO inventory(name, storage, amount) VALUES(?, ?, ?)";
try(Connection conn = Database.getConnection()){
PreparedStatement stmt = conn.prepareStatement(sql);

}
catch(SQLException e){
throw new SQLException("Error Inserting Data Into Inventory");
}
}
}