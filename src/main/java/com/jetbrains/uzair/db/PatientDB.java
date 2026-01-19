package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientDB {
    public static void insert(Patient p){
        String sql = "INSERT INTO patients(name, age, gender) VALUES( ?, ?, ?)";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            stmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error Inserting Data Into 'patients': " + e.getMessage());
        }
    }
    public static
}