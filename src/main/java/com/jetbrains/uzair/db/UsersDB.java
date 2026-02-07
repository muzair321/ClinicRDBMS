package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDB {
    public static String getName(int id) throws SQLException {
        String sql = "SELECT username FROM users WHERE id = ?";
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString("username");
            }
            return "Error Occurred";
        } catch (SQLException e) {
            throw new SQLException("Error Fetching User Name");
        }
    }
}
