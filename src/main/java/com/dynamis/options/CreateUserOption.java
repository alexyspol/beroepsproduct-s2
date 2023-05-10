package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.SQLFile;

public class CreateUserOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        SQLFile sql = new SQLFile("create_user.sql");

        // Ask for information

        System.out.println();

        System.out.print("First name: ");
        String firstName = s.nextLine().trim();
        if(firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }

        System.out.print("Last name: ");
        String lastName = s.nextLine().trim();
        if(lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }

        System.out.print("Student ID: ");
        String studentId = s.nextLine().trim().toUpperCase();
        if(studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be blank");
        }
        if(!studentId.matches("^[A-Z]{2}/\\d{4}/\\d{3}$")) {
            throw new IllegalArgumentException("Invalid Student ID format");
        }

        System.out.print("Date of birth (yyyy-mm-dd): ");
        String dob = s.nextLine().trim();
        LocalDate.parse(dob); // will throw if invalid

        System.out.print("Team name: ");
        String teamName = s.nextLine().trim();
        if(teamName.isEmpty()) {
            throw new IllegalArgumentException("User must be assigned to a team");
        }

        System.out.print("Phone number: (+597) ");
        String phone = s.nextLine().trim();
        if(!phone.matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}")) {
            throw new IllegalArgumentException("Invalid phone number format. Valid formats are 0xxx-xxxx, xxx-xxxx or xxxxxxx (7 digits)");
        }

        System.out.print("E-mail: ");
        String email = s.nextLine().trim();
        if(!email.matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        System.out.print("Residence: ");
        String residence = s.nextLine().trim();

        // TODO Make sure the input really is just one skill
        System.out.print("Skill (only one): ");
        String skill = s.nextLine().trim();

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement findOutIfTeamExists = c.prepareStatement(sql.nextStatement());
            PreparedStatement createNewTeam = c.prepareStatement(sql.nextStatement(), Statement.RETURN_GENERATED_KEYS);
            PreparedStatement createNewUser = c.prepareStatement(sql.nextStatement());
            PreparedStatement saveContactInfo = c.prepareStatement(sql.nextStatement())) {

            int teamId;

            findOutIfTeamExists.setString(1, teamName);
            try(ResultSet rs = findOutIfTeamExists.executeQuery()) {
                teamId = rs.getInt("id");
            }

            // Only add the team to the teams table if it doesn't already exist

            if(teamId == 0) {
                createNewTeam.setString(1, teamName);
                createNewTeam.executeUpdate();
            
                try(ResultSet generatedKeys = createNewTeam.getGeneratedKeys()) {
                    teamId = generatedKeys.getInt(1);
                }
            }

            // Insert into users table

            createNewUser.setString(1, studentId);
            createNewUser.setString(2, firstName);
            createNewUser.setString(3, lastName);
            createNewUser.setString(4, dob);
            createNewUser.setInt(5, teamId);
            createNewUser.executeUpdate();

            // Insert into contact_info table

            saveContactInfo.setString(1, studentId);
            saveContactInfo.setString(2, phone);
            saveContactInfo.setString(3, email);
            saveContactInfo.setString(4, residence);
            saveContactInfo.setString(5, skill);
            saveContactInfo.executeUpdate();
        }

        System.out.printf("""

        > Created new user: %s %s
            Student ID: %s
            Date of birth: %s
            Team: %s
            Phone number: (+597) %s
            E-mail address: %s
            Residence: %s
            Skill: %s

        """, firstName, lastName, studentId, dob, teamName, phone, email, residence, skill);
    }

    @Override
    public String toString() {
        return "Create user";
    }

}
