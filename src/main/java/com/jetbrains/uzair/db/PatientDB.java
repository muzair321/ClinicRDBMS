package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PatientDB {
    //Create
    public static void insert(Patient p) throws SQLException{
        String sql = "INSERT INTO patients(name, age, gender, phone) VALUES( ?, ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getPhone());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException("Error Inserting Data Into 'patients': " + e.getMessage());
        }
    }
    //Read all
    public static String[][] returnUI() throws SQLException{
        String[][] returnSet;
        int count = 0;
        String sql = "SELECT * FROM patients";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                count++;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Counting Rows In Database: " + e.getMessage());
        }
        returnSet = new String[count][5];
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            for(int i = 0; rs.next() && i < returnSet.length; i++){
                returnSet[i][0] = Integer.toString(rs.getInt(1));
                returnSet[i][1] = rs.getString(2);
                returnSet[i][2] = Integer.toString(rs.getInt(3));
                returnSet[i][3] = rs.getString(4);
                returnSet[i][4] = rs.getString(5);
            }
        } catch (SQLException e) {
            throw new SQLException("Error Reading Database: " + e.getMessage());
        }
        return returnSet;
    }
    //Read By id
    public static Patient returnUISingle(int id) throws SQLException {
        String sql = "SELECT id, name, age, gender, phone FROM patients WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {throw new SQLException("Patient not found with id: " + id);}
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                return p;
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving patient (id: " + id + "): " + e.getMessage());
        }
    }
    //Edit
    public static void edit(Patient p) throws SQLException{
        String sql = "UPDATE patients SET name =  ?, age = ?, gender = ?, phone = ? WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt( 5, p.getId());
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            stmt.setString(4, p.getPhone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Editing Database: " + e.getMessage());
        }
    }
    //Delete
    // Delete by single id
    public static void delete(int id) throws SQLException{
        String sql = "DELETE FROM patients WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Patient id(" + id + "): " + e.getMessage());
        }
    }
    // delete by list of ids for multi selected patients
    public static void deleteList(List<Integer> list) throws SQLException{
        String sql = "DELETE FROM patients WHERE id = ?";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int id: list){
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error Deleting Patient List: " + e.getMessage());
        }
    }
    public static boolean checkPhone(String phone) {
        if (phone == null) return true;
        if (phone.trim().isEmpty()) return false;
        String sql = "SELECT 1 FROM patients WHERE phone = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}