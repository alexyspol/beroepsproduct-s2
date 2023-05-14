package com.dynamis.options;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import com.dynamis.App;
import com.dynamis.utils.SQLFileReader;

public class DisplayUsersOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        String sql = SQLFileReader.readSQLFile("display_users.sql").get("select_everything");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement s = c.prepareStatement(sql);
            ResultSet rs = s.executeQuery()) {

            int i = 1;

            System.out.println();
            while(rs.next()) {
                String studentId = rs.getString("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String dob = rs.getString("dob");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String residence = rs.getString("residence");
                String skill = rs.getString("skill");
                String teamName = rs.getString("team_name");

                System.out.printf("""
                    > %d. %s %s
                        Student ID: %s
                        Date of Birth: %s
                        Age: %d
                        Team: %s
                        Phone: %s
                        E-mail: %s
                        Residence: %s
                        Skill: %s

                        """, i, firstName, lastName, studentId, dob, calculateAge(dob), filterNulls(teamName), phone, email, residence, skill);

                i++;
            }
        }
    }

    private int calculateAge(String dob) {
        LocalDate birthDate = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    private String filterNulls(String str) {
        if(str == null) {
            return "---";
        }
        else {
            return str;
        }
    }

    @Override
    public String toString() {
        return "Display list of users";
    }

}
