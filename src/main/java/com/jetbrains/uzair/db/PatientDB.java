package com.jetbrains.uzair.db;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.jetbrains.uzair.model.Patient;

public class PatientDB{
    public static void addPatient(Patient p){
        String sql = "INSERT INTO patients(phone, name, age, gender) VALUES(?, ?, ?, ?)";
        try(Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, p.getPhone());
            stmt.setString(2, p.getName());
            stmt.setInt(3,p.getAge());
            stmt.setString(4, p.getGender());
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}