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
        String sql = "INSERT INTO visits(patient_id, illness, treatment, paid)  VALUES(?, ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, v.getPatientId());
            stmt.setString(2, v.getIll());
            stmt.setString(3, v.getTreatment());
            stmt.setInt(4, v.getPaid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static void edit(Visit v) throws SQLException{
        String sql = "UPDATE visits SET patient_id = ?, illness = ?, treatment = ?, paid = ? WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt( 5, v.getId());
            stmt.setInt(1, v.getPatientId());
            stmt.setString(2, v.getIll());
            stmt.setString(3, v.getTreatment());
            stmt.setInt(4, v.getPaid());
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
               v.illness,
               v.date
        FROM visits v
        JOIN patients p ON p.id = v.patient_id
        ORDER BY v.id DESC;
    """;
        List<String[]> rows = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        "PAT-" + String.valueOf(rs.getInt("patient_id")),
                        rs.getString("patient_name"),
                        rs.getString("illness"),
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
               v.illness,
               v.treatment,
               v.date
        FROM visits v
        JOIN patients p ON p.id = v.patient_id
        WHERE p.name LIKE ?
            OR date LIKE ?
            OR v.patient_id LIKE ?
        ORDER BY v.id DESC;
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            String.valueOf(rs.getInt("patient_id")),
                            rs.getString("patient_name"),
                            rs.getString("illness"),
                            rs.getString("date")
                    });
                }
            }
        }
        return rows.toArray(new String[0][]);
    }
    public static Visit returnUISingle(int id) throws SQLException{
        String sql = "SELECT id, patient_id, illness, treatment, paid, date FROM visits WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {throw new SQLException("Visit Not Found With Id: " + id);}
                Visit v = new Visit();
                v.setId(rs.getInt("id"));
                v.setPatientId(rs.getInt("patient_id"));
                v.setIll(rs.getString("illness"));
                v.setTreatment(rs.getString("treatment"));
                v.setPaid(rs.getInt("paid"));
                v.setDate(rs.getString("date"));
                return v;
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving Visit (id: " + id + "): " + e.getMessage());
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
    public static void deleteList(List<Integer> list) throws SQLException{
        String sql = "DELETE FROM visits WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int id: list){
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Visit List: " + e.getMessage());
        }
    }
    public static List<Visit> returnVisits(int patientId) throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE patient_id = ? ORDER BY date DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Visit v = new Visit();
                v.setId(rs.getInt("id"));
                v.setPatientId(rs.getInt("patient_id"));
                v.setIll(rs.getString("illness"));
                v.setTreatment(rs.getString("treatment"));
                v.setPaid(rs.getInt("paid"));
                v.setDate(rs.getString("date"));
                visits.add(v);
            }
        }
        return visits;
    }
}