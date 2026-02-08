package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.User;
import com.jetbrains.uzair.security.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public static void exists(String user) throws SQLException{
        String sql = "SELECT id FROM users WHERE username = ?";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                throw new SQLException("Username Already Exists");
            }
        }
    }
    public static void insert(String username, String password, int admin) throws SQLException{
        if(admin != 1 && admin != 0){
            throw new SQLException("Admin State Non-Binary");
        }
        if (username == null || username.isBlank()) {
            throw new SQLException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new SQLException("Password cannot be empty");
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
    public static void delete(List<Integer> list) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int id : list) {

                if (id != 1) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error Deleting User List: " + e.getMessage());
        }
    }
    public static void changeAdminPassword(String pass) throws SQLException{
        String sql = "UPDATE users SET password = ? WHERE id = 1";
        if (pass == null || pass.isBlank()) {
            throw new SQLException("Password cannot be empty");
        }
        String hash = PasswordUtil.hash(pass);
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, hash);
            stmt.executeUpdate();
        }
    }
    public static String[][] returnUI() throws SQLException{
        String[][] returnSet;
        String state;
        int count = 0;
        String sql = """
        SELECT id,
               username,
               admin
        FROM users
        """;
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                count++;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Retrieving Data From 'users'");
        }
        returnSet = new String[count][3];
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            for (int i = 0; rs.next() && i < returnSet.length; i++){
                state = rs.getInt("admin") == 1 ? "YES" : "NO";
                returnSet[i][0] = rs.getString("id");
                returnSet[i][1] = rs.getString("username");
                returnSet[i][2] = state;
            }
            return returnSet;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
}
