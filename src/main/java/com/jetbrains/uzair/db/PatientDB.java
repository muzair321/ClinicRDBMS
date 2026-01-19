package com.jetbrains.uzair.db;

import com.jetbrains.uzair.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public static String[][] returnUI(){
        String[][] returnSet;
        int count = 0;
        String sql = "SELECT * FROM patients";
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                count++;
            }
        } catch (Exception e) {
            System.err.println("Error Counting Rows In Database: " + e.getMessage());
        }
        returnSet = new String[count][4];
        try(Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            for(int i = 0; rs.next() && i < returnSet.length; i++){
                returnSet[i][0] = Integer.toString(rs.getInt(1));
                returnSet[i][1] = rs.getString(2);
                returnSet[i][2] = Integer.toString(rs.getInt(3));
                returnSet[i][3] = rs.getString(4);
            }
        } catch (Exception e) {
            System.err.println("Error Reading Database: " + e.getMessage());
        }
        return returnSet;
    }
}