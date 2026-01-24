package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class VisitDB {
    public static void insert(Visit v){
        String sql = "INSERT INTO visits(patient_id, treatment, paid)  VALUES(?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, v.getPatientId());
            stmt.setString(2, v.getTreatment());
            stmt.setInt(3, v.getPaid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static String[][] returnUI() throws SQLException {
        String sql = """
        SELECT v.id,
               v.patient_id,
               p.name AS patient_name,
               v.treatment,
               v.paid,
               v.date
        FROM visits v
        JOIN patients p ON p.id = v.patient_id
    """;
        List<String[]> rows = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("patient_id")),
                        rs.getString("patient_name"),
                        rs.getString("treatment"),
                        String.valueOf(rs.getInt("paid")),
                        rs.getString("date")
                });
            }
        } catch (SQLException e) {
            throw new SQLException("Error Reading Visits Data: " + e.getMessage(), e);
        }
        return rows.toArray(new String[0][0]);
    }
}