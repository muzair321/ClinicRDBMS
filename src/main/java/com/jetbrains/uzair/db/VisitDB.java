package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Patient;
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
    public static void edit(Visit v) throws SQLException{
        String sql = "UPDATE visits SET patient_id = ?, treatment = ?, paid = ? WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt( 4, v.getId());
            stmt.setInt(1, v.getPatientId());
            stmt.setString(2, v.getTreatment());
            stmt.setInt(3, v.getPaid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Editing Database: " + e.getMessage());
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
    public static String[][] returnUI(String searchTerm) throws SQLException {
        List<String[]> rows = new ArrayList<>();

        String sql = """
        SELECT v.id,
               v.patient_id,
               p.name AS patient_name,
               v.treatment,
               v.paid,
               v.date
        FROM visits v
        JOIN patients p ON p.id = v.patient_id
        WHERE p.name LIKE ?
            OR date LIKE ?
            OR v.patient_id LIKE ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                java.sql.ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    rows.add(row);
                }
            }
        }

        // Convert List to array
        return rows.toArray(new String[0][]);
    }
    public static Visit returnUISingle(int id) throws SQLException{
        String sql = "SELECT id, patient_id, treatment, paid, date FROM visits WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {throw new SQLException("Visit Not Found With Id: " + id);}
                Visit v = new Visit();
                v.setId(rs.getInt("id"));
                v.setPatientId(rs.getInt("patient_id"));
                v.setTreatment(rs.getString("treatment"));
                v.setPaid(rs.getInt("paid"));
                v.setDate(rs.getString("date"));
                return v;
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving patient (id: " + id + "): " + e.getMessage());
        }
    }
    public static void delete(int id) throws SQLException{
        String sql = "DELETE FROM visits WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Visit id(" + id + "): " + e.getMessage());
        }
    }
}