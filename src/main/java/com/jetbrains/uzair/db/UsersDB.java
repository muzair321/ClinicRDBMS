package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.User;
import com.jetbrains.uzair.security.PasswordUtil;

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
        String sql = "SELECT id, username, password, admin FROM users WHERE username=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (!PasswordUtil.verify(password, storedHash)) {
                    return null;
                }
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
    public static void insert(String username, String password, int admin) throws SQLException{
        if(admin != 1 && admin != 0){
            throw new SQLException("Error: Admin State Non-Binary");
        }
        if (username == null || username.isBlank()) {
            throw new SQLException("Error: Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new SQLException("Error: Password cannot be empty");
        }

        String hash = PasswordUtil.hash(password);
        String sql = "INSERT INTO users( username, password, admin) VALUES( ?, ?, ?)";
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, hash);
            stmt.setInt(3, admin);
            stmt.executeUpdate();
        }
    }
}
