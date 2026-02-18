package com.jetbrains.uzair.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class AnalyticsDB {
    public static LinkedHashMap<String, Integer> visitsToday(){
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
    public static LinkedHashMap<String, Integer> ageToday(){
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        String[] keys = {"0-2", "3-12", "13-20", "21-35", "36-55", "56-65", "66-75", "76-100+"};
        int[] groups = {0, 0, 0, 0, 0, 0, 0, 0};
        String sql= """
                SELECT p.age AS age, COUNT(*) AS count
                FROM visits v
                JOIN patients p ON p.id = v.patient_id
                WHERE date(date) = date('now')
                GROUP BY age;
                """;
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                int num = rs.getInt("age");
                int count = rs.getInt("count");
                if(num <= 2){
                    groups[0] += count;
                } else if (num <= 12) {
                    groups[1] += count;
                } else if (num <= 20) {
                    groups[2] += count;
                } else if (num <= 35) {
                    groups[3] += count;
                } else if (num <= 55) {
                    groups[4] += count;
                } else if (num <= 65) {
                    groups[5] += count;
                } else if (num <= 75) {
                    groups[6] += count;
                }else {
                    groups[7] += count;
                }
                int i = 0;
                for (String key: keys){
                    data.put(key, groups[i]);
                    i++;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }
}
