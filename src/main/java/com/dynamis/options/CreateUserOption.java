package com.dynamis.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.dynamis.App;

public class CreateUserOption implements Option {

    private String[] sql = {
        """
            SELECT id
            FROM teams
            WHERE team_name = ?;
        """,
        """
            INSERT INTO teams (team_name)
            VALUES (?);
        """,
        """
            INSERT INTO users (student_id, first_name, last_name, dob, team_id)
            VALUES (?, ?, ?, ?, ?);
        """,
        """
            INSERT INTO contact_info (student_id, phone, email, residence, skill)
            VALUES (?, ?, ?, ?, ?);
        """
    };

    @Override
    public void run(App app) throws SQLException {
        Connection c = app.getConnection();
        Scanner s = app.getScanner();

        s.nextLine(); // consume the previous newline character
        
        PreparedStatement selectTeamIdIfExists = c.prepareStatement(sql[0]);
        PreparedStatement insertNewTeam = c.prepareStatement(sql[1], Statement.RETURN_GENERATED_KEYS);
        PreparedStatement insertNewUser = c.prepareStatement(sql[2]);
        PreparedStatement insertContactInfo = c.prepareStatement(sql[3]);

        // Step 1: Ask for the user's first name

        System.out.print("\nFirst name: ");
        String firstName = s.nextLine().trim();

        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }

        // Step 2: Ask for the user's last name

        System.out.print("Last name: ");
        String lastName = s.nextLine().trim();

        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }

        // Step 3: Ask for the user's student ID

        System.out.print("Student ID: ");
        String studentId = s.nextLine().trim();

        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be blank");
        }

        // Step 4: Ask for the user's date of birth

        System.out.print("Date of birth (yyyy-mm-dd): ");
        String dob = s.nextLine().trim();

        if (!isValidDateString(dob)) {
            throw new IllegalArgumentException("Invalid date format, expected yyyy-mm-dd");
        }

        // Step 5: Ask for the user's team name

        System.out.print("Team name: ");
        String teamName = s.nextLine().trim();

        if(teamName.isEmpty()) {
            throw new IllegalArgumentException("User must be assigned to a team");
        }

        // Step 6: Ask for the user's phone number

        System.out.print("Phone number: (+597) ");
        String phone = s.nextLine().trim();

        if(!isValidPhoneNumber(phone)) {
            throw new IllegalArgumentException("Invalid phone number format. Valid formats are 0xxx-xxxx, xxx-xxxx or xxxxxxx (7 digits)");
        }

        // Step 7: Ask for the user's e-mail address

        System.out.print("E-mail: ");
        String email = s.nextLine().trim();

        if(!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Step 8: Ask for the user's residence

        System.out.print("Residence: ");
        String residence = s.nextLine().trim();

        // Step 9: Ask for the user's most prominent skill (only one)

        // TODO Make sure the input really is just one skill
        System.out.print("Skill (only one): ");
        String skill = s.nextLine().trim();

        // Step 10: Create a new team if it doesn't exist

        selectTeamIdIfExists.setString(1, teamName);
        ResultSet rs = selectTeamIdIfExists.executeQuery();

        int teamId = rs.getInt("id");

        if(teamId == 0) {
            insertNewTeam.setString(1, teamName);
            insertNewTeam.executeUpdate();

            ResultSet generatedKeys = insertNewTeam.getGeneratedKeys();
            generatedKeys.next();
            teamId = generatedKeys.getInt(1);
        }

        // Step 11: Create the new user

        insertNewUser.setString(1, studentId);
        insertNewUser.setString(2, firstName);
        insertNewUser.setString(3, lastName);
        insertNewUser.setString(4, dob);
        insertNewUser.setInt(5, teamId);
        insertNewUser.executeUpdate();

        // Step 12: Store the user's contact information

        insertContactInfo.setString(1, studentId);
        insertContactInfo.setString(2, phone);
        insertContactInfo.setString(3, email);
        insertContactInfo.setString(4, residence);
        insertContactInfo.setString(5, skill);
        insertContactInfo.executeUpdate();

        // Step : Print information

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

    private boolean isValidDateString(String dateString) {
        return dateString.matches("^\\d{4}-\\d{2}-\\d{2}$"); // regular expression to match yyyy-mm-dd pattern
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}");
    }

    @Override
    public String toString() {
        return "Create user";
    }

}
