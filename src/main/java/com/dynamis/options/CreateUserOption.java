package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.SQLFileReader;
import com.dynamis.Triplee;
import com.dynamis.validators.*;
import com.dynamis.validators.decorators.*;

public class CreateUserOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("create_user.sql");
        Map<String, Object> newUser = new HashMap<>();

        // Ask for information

        List<Triplee> xx = new ArrayList<>(); // TODO Better variable name
        xx.add(new Triplee("First name", "first_name", new IsNotEmpty(new StringValidator())));
        xx.add(new Triplee("Last name", "last_name", new IsNotEmpty(new StringValidator())));
        xx.add(new Triplee("Student ID", "student_id", new StudentID(new StringValidator())));
        xx.add(new Triplee("Date of Birth", "dob", new DateString(new StringValidator())));
        xx.add(new Triplee("Team", "team_name", new TeamExists(new StringValidator())));
        xx.add(new Triplee("Phone number", "phone", new PhoneNumber(new StringValidator())));
        xx.add(new Triplee("E-mail", "email", new Email(new StringValidator())));
        xx.add(new Triplee("Residence", "residence", new StringValidator()));
        xx.add(new Triplee("Skill", "skill", new StringValidator()));
        
        System.out.println();

        for(Triplee x : xx) {
            String request = x.getRequest();
            String columnName = x.getColumnName();
            IValidator validator = x.getValidator();

            String answer;

            do {
                System.out.printf("%s: ", request);
                answer = s.nextLine().trim();

                validator.setValue(answer);

            } while(!validator.isValid());

            newUser.put(columnName, answer);
        }

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectTeam = c.prepareStatement(sql.get("select_team_by_id"));
            PreparedStatement createNewUser = c.prepareStatement(sql.get("create_new_user"));
            PreparedStatement saveContactInfo = c.prepareStatement(sql.get("save_contact_info"))) {

            selectTeam.setString(1, (String) newUser.get("team_name"));

            try(ResultSet rs2 = selectTeam.executeQuery()) {
                newUser.put("team_id", rs2.getInt("id"));
            }

            // Insert into users table

            createNewUser.setString(1, (String) newUser.get("student_id"));
            createNewUser.setString(2, (String) newUser.get("first_name"));
            createNewUser.setString(3, (String) newUser.get("last_name"));
            createNewUser.setString(4, (String) newUser.get("dob"));
            createNewUser.setInt(5, (int) newUser.get("team_id"));
            createNewUser.executeUpdate();

            // // Insert into contact_info table

            saveContactInfo.setString(1, (String) newUser.get("student_id"));
            saveContactInfo.setString(2, (String) newUser.get("phone"));
            saveContactInfo.setString(3, (String) newUser.get("email"));
            saveContactInfo.setString(4, (String) newUser.get("residence"));
            saveContactInfo.setString(5, (String) newUser.get("skill"));
            saveContactInfo.executeUpdate();
        }

        System.out.println("\n> User created\n");
    }

    @Override
    public String toString() {
        return "Create user";
    }

}
