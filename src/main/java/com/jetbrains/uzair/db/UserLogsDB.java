package com.jetbrains.uzair.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserLogsDB {
    public static void insert(int userId)throws SQLException {
        String sql = "INSERT INTO user_logs(user_id) VAlUES(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Logging User Info");
        }
    }
    public static String[][] returnUI() throws SQLException{
        List<String[]> list = new ArrayList<>();
        String sql = """
                SELECT ul.id,
                    ul.user_id,
                    u.username AS name,
                    ul.date
                FROM user_logs ul
                JOIN users u ON u.id = ul.user_id
                """;
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                list.add(new String[]{
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("date")
                });
            }

        }
        return list.toArray(new String[0][]);
    }
    public static String[][] returnUI(String search) throws SQLException{
        List<String[]> list = new ArrayList<>();
        String sql = """
                SELECT ul.id,
                    ul.user_id,
                    u.username AS name,
                    ul.date
                FROM user_logs ul
                JOIN users u ON u.id = ul.user_id
                WHERE ul.user_id LIKE ?
                    OR u.username LIKE ?
                    OR ul.date LIKE ?
                """;
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + search + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("user_id"),
                            rs.getString("name"),
                            rs.getString("date")
                    });
                }
            }
        }
        return list.toArray(new String[0][]);
    }
}
