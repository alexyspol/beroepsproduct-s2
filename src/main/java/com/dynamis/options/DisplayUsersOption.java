package com.dynamis.options;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;

import com.dynamis.App;
import com.dynamis.SqlFileReader;

public class DisplayUsersOption implements Option {

    @Override
    public void run(App app) throws SQLException, IOException {
        System.out.println();

        Statement s = app.getConnection().createStatement();
        ResultSet rs = s.executeQuery(SqlFileReader.read("src/main/resources/sqlite/display_users.sql"));

        int i = 1;

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

            System.out.println(
                "> " + i + ". " + firstName + " " + lastName + "\n" +
                "    Student ID: " + studentId + '\n' +
                "    Date of Birth: " + dob + "\n" +
                "    Age: " + calculateAge(dob) + '\n' +
                "    Team: " + teamName + '\n' +
                "    Phone: " + phone + '\n' +
                "    E-mail: " + email + '\n' +
                "    Residence: " + residence + '\n' +
                "    Skill: " + skill + '\n'
            );

            i++;
        }
    }

    private int calculateAge(String dob) {
        LocalDate birthDate = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
