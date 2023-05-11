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
import com.dynamis.SQLFile;
import com.dynamis.validators.Validator;
import com.dynamis.validators.ValidatorFactory;

public class EditUserByIdOption implements Option {

    private static String[][] xx = {   // TODO Better variable name
        { "First name", "first_name" },
        { "Last name", "last_name" },
        { "Student ID", "student_id" },
        { "Date of birth", "dob" },
        { "Team", "team_name" },
        { "Phone number", "phone" },
        { "E-mail", "email" },
        { "Residence", "residence" },
        { "Skill", "skill" }
    };

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        SQLFile sql = new SQLFile("edit_user.sql");

        Map<String, Object> selectedUser;
        Map<String, String> changes = new HashMap<>();

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllUsers = c.prepareStatement(sql.nextStatement());
            PreparedStatement selectRelatedInfo = c.prepareStatement(sql.nextStatement());
            PreparedStatement updateUser = c.prepareStatement(sql.nextStatement());
            PreparedStatement updateTeam = c.prepareStatement(sql.nextStatement());
            PreparedStatement updateContactInfo = c.prepareStatement(sql.nextStatement())) {

            List<Map<String, Object>> users = new ArrayList<>();

            try(ResultSet rs = selectAllUsers.executeQuery()) {
                while(rs.next()) {
                    String studentId = rs.getString("student_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String dob = rs.getString("dob");
                    int teamId = rs.getInt("team_id");

                    Map<String, Object> user = new HashMap<>();
                    user.put("student_id", studentId);
                    user.put("first_name", firstName);
                    user.put("last_name", lastName);
                    user.put("dob", dob);
                    user.put("team_id", teamId);

                    users.add(user);
                }
            }

            // Pick one

            System.out.println("\nEdit user:");
            for(int i = 0; i < users.size(); i++) {
                Map<String, Object> user = users.get(i);
                System.out.printf("%d. %s %s\n", i+1, user.get("first_name"), user.get("last_name"));
            }
    
            int selection = s.nextInt();
            s.nextLine(); // consume the previous newline character

            if(!(1 <= selection && selection <= users.size())) {
                throw new IllegalArgumentException("Your answer needs to be between 1 and " + users.size());
            }

            selectedUser = users.get(selection - 1);

            // Only continue if you supply the right student ID for this user

            System.out.print("\nEnter Student ID: ");
            String userInput = s.nextLine().trim();
            if(!(userInput.contentEquals((String) selectedUser.get("student_id")))) {
                throw new IllegalArgumentException("Invalid student ID: " + userInput);
            }
            System.out.println();

            // Gather all information about the user

            selectRelatedInfo.setInt(1, (int) selectedUser.get("team_id"));
            selectRelatedInfo.setString(2, (String) selectedUser.get("student_id"));

            try(ResultSet rs = selectRelatedInfo.executeQuery()) {
                String teamName = rs.getString("team_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String residence = rs.getString("residence");
                String skill = rs.getString("skill");
            
                selectedUser.put("team_name", teamName);
                selectedUser.put("phone", phone);
                selectedUser.put("email", email);
                selectedUser.put("residence", residence);
                selectedUser.put("skill", skill);
            }

            // Ask for changes

            for(String[] x : xx) {
                String question = x[0];
                String columnName = x[1];
                String currentValue = (String) selectedUser.get(columnName);

                Validator validator = ValidatorFactory.create(columnName);
                String answer;

                do {
                    System.out.printf("%s (%s): ", question, currentValue);
                    answer = s.nextLine().trim();

                } while(!answer.isEmpty() && !validator.isValid(answer));

                if(!answer.isEmpty()) {
                    changes.put(columnName, answer);
                }
            }

            // Merge values

            Map<String, Object> merged = new HashMap<>();

            for(Map.Entry<String, Object> entry : selectedUser.entrySet()) {
                String key = entry.getKey();
                Object value = changes.containsKey(key) ? changes.get(key) : entry.getValue();
                merged.put(key, value);
            }

            // Update users table

            updateUser.setString(1, (String) merged.get("first_name"));
            updateUser.setString(2, (String) merged.get("last_name"));
            updateUser.setString(3, (String) merged.get("dob"));
            updateUser.setString(4, (String) merged.get("student_id"));
            updateUser.executeUpdate();

            // Update teams table

            updateTeam.setString(1, (String) merged.get("team_name"));
            updateTeam.setInt(2, (int) merged.get("team_id"));
            updateTeam.executeUpdate();

            // Update contact_info table

            updateContactInfo.setString(1, (String) merged.get("phone"));
            updateContactInfo.setString(2, (String) merged.get("email"));
            updateContactInfo.setString(3, (String) merged.get("residence"));
            updateContactInfo.setString(4, (String) merged.get("skill"));
            updateContactInfo.setString(5, (String) merged.get("student_id"));
            updateContactInfo.executeUpdate();
        }

        // Print information

        if(changes.size() == 0) {
            System.out.println("\n> No changes made\n");
            return;
        }

        System.out.println("\n> Changes made:");
        for(String[] x : xx) {
            String label = x[0];
            String columnName = x[1];

            if(changes.containsKey(columnName)) {
                String oldValue = (String) selectedUser.get(columnName);
                String newValue = changes.get(columnName);

                System.out.printf("    %s: \"%s\" to \"%s\"\n", label, oldValue, newValue);
            }
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Edit user by id";
    }

}
