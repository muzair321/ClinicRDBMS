package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.User;

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
    public static User authenticate(String username, String password) throws SQLException{
        String sql = "SELECT id, username, admin FROM users WHERE username=? AND password=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password); // later: hashed

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("admin") == 1
                );
            }

        } catch (SQLException e) {
            throw new SQLException("Error Logging In: " + e.getMessage());
        }
        return null;
    }
}
