package com.jetbrains.uzair.db;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class AnalyticsDB {
    public static LinkedHashMap<String, Integer> visits(){
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        String sql = """
                SELECT strftime('%H', date) AS hour, COUNT(*) AS visits
                    FROM visits
                    WHERE date(date) = date('now')
                    GROUP BY hour;
                """;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ){
            for (int i = 0; i < 25; i++) {
                data.put(i + ":00", 0);
            }
            while (rs.next()) {
                for (int i = 0; i < 25; i++) {
                    if (rs.getInt("hour") == i) {
                        data.put(i + ":00", rs.getInt("visits"));
                    }
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static LinkedHashMap<String, Integer> genderToday(){
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        String[] keys = {"Male", "Female", "Other"};
        String sql= """
                SELECT p.gender AS sex, COUNT(*) AS visitors
                FROM visits v
                JOIN patients p ON p.id = v.patient_id
                WHERE date(date) = date('now')
                GROUP BY sex;
                """;
        try(Connection conn = Database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                System.out.println(rs.getString("sex") +rs.getInt("visitors"));
                for (String key: keys) {
                    if (rs.getString("sex").equals(key)) {
                        data.put(key, rs.getInt("visitors"));
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }
}
