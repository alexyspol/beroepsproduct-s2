package com.dynamis.options;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;

import com.dynamis.App;

public class DisplayUsersOption implements Option {
    private String sql = """
        SELECT u.student_id, u.first_name, u.last_name, u.dob,
               c.phone, c.email, c.residence, c.skill,
               t.team_name
        FROM users u, contact_info c, teams t
        WHERE c.student_id = u.student_id AND t.id = u.team_id;
        """;

    @Override
    public void run(App app) throws SQLException, IOException {

        // Step 1: Fetch information from database

        Statement s = app.getConnection().createStatement();
        ResultSet rs = s.executeQuery(sql);

        // Step 2: Print it out

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

                    """, i, firstName, lastName, studentId, dob, calculateAge(dob), teamName, phone, email, residence, skill);

            i++;
        }
    }

    private int calculateAge(String dob) {
        LocalDate birthDate = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
