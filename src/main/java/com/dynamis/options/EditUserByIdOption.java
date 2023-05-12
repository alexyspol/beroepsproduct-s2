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
            int selection;
            boolean isValidInput;

            do {
                System.out.println("\nEdit user:");
                for(int i = 0; i < users.size(); i++) {
                    Map<String, Object> user = users.get(i);
                    System.out.printf("%d. %s %s\n", i+1, user.get("first_name"), user.get("last_name"));
                }
        
                selection = s.nextInt();
                s.nextLine(); // consume the previous newline character

                isValidInput = (1 <= selection && selection <= users.size());

                if(!isValidInput) {
                    System.out.println("\n> Your answer needs to be between 1 and " + users.size());
                }

            } while(!isValidInput);
            
            selectedUser = users.get(selection - 1);

            // Only continue if you supply the right student ID for this user

            int numChances = 3;
            int numTurns = 0;

            do {
                System.out.printf("\nEnter %s %s's Student ID to continue: ", selectedUser.get("first_name"), selectedUser.get("last_name"));
                String userInput = s.nextLine().trim();

                isValidInput = (userInput.contentEquals((String) selectedUser.get("student_id")));
                numTurns++;

                if(!isValidInput) {
                    System.out.printf("> Invalid student ID (%d/%d chances)\n", numTurns, numChances);
                }

            } while(!isValidInput && (numTurns < numChances));

            System.out.println();

            if(!isValidInput) {
                return;
            }

            // Gather all information about the user

            selectRelatedInfo.setInt(1, (int) selectedUser.get("team_id"));
            selectRelatedInfo.setString(2, (String) selectedUser.get("student_id"));

            try(ResultSet rs = selectRelatedInfo.executeQuery()) {
                selectedUser.put("team_name", rs.getString("team_name"));
                selectedUser.put("phone", rs.getString("phone"));
                selectedUser.put("email", rs.getString("email"));
                selectedUser.put("residence", rs.getString("residence"));
                selectedUser.put("skill", rs.getString("skill"));
            }

            // Ask for changes

            String[][] xx = {   // TODO Better variable name
                { "First name", "first_name" },
                { "Last name", "last_name" },
                { "Student ID", "student_id" }, // TODO Find a way to change this without primary key conflicts.
                { "Date of birth", "dob" },
                { "Team", "team_name" },
                { "Phone number", "phone" },
                { "E-mail", "email" },
                { "Residence", "residence" },
                { "Skill", "skill" }
            };

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

        String[] usersTableColumns = { "first_name", "last_name", "dob" };
        String[] teamsTableColumns = { "team_name" };
        String[] contactInfoTableColumns = { "phone", "email", "residence", "skill" };

        Map<String, String[]> tables = new HashMap<>();
        tables.put("users", usersTableColumns);
        tables.put("teams", teamsTableColumns);
        tables.put("contact_info", contactInfoTableColumns);

        boolean tableNameAdded = false;
        StringBuilder message = new StringBuilder();

        for(Map.Entry<String, String[]> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            String[] tableColumns = entry.getValue();

            for(String column : tableColumns) {
                if(changes.containsKey(column)) {
                    if(!tableNameAdded) {
                        message.append("    " + tableName + " -> ");
                        tableNameAdded = true;
                    }
                    message.append(column + ", ");
                }
            }

            if(tableNameAdded) {
                message.delete(message.length() - 2, message.length());
                message.append("\n");
            }
            tableNameAdded = false;
        }

        if(message.isEmpty()) {
            System.out.println("\n> No changes made\n");
        }
        else {
            System.out.println("\n> Changes made:\n" + message);
        }
    }

    @Override
    public String toString() {
        return "Edit user by id";
    }

}
