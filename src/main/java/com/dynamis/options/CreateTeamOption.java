package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.utils.SQLFileReader;
import com.dynamis.validators.InRange;
import com.dynamis.validators.IntegerValidator;
import com.dynamis.validators.StringValidator;
import com.dynamis.validators.UniqueTeamName;
import com.dynamis.validators.Validator;

public class CreateTeamOption implements Option {

    @Override
    public void run(App app) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("create_team.sql");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllUsers = c.prepareStatement(sql.get("select_all_teams_users"));
            PreparedStatement createNewTeam = c.prepareStatement(sql.get("create_new_team"));
            PreparedStatement addUsers = c.prepareStatement(sql.get("add_users_to_team"));
            ResultSet rs = selectAllUsers.executeQuery()) {

            List<Map<String, String>> users = new ArrayList<>();

            while(rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("student_id", rs.getString("student_id"));
                user.put("first_name", rs.getString("first_name"));
                user.put("last_name", rs.getString("last_name"));
                user.put("team_name", rs.getString("team_name"));
                users.add(user);
            }

            // Enter new team name

            System.out.println();
            Validator validator = new UniqueTeamName(new StringValidator());

            do {
                System.out.print("Create new team: ");
                validator.setValue(s.nextLine().trim());

            } while(!validator.isValid());

            String newTeamName = (String) validator.getValue();

            // Select at least one user to add to this team

            List<Map<String, String>> selectedUsers = new ArrayList<>();
            validator = new InRange(new IntegerValidator(), 1, users.size());
            boolean isSelectionValid = false;

            do {
                System.out.println("\nAdd to the team:");
                for(int i = 0; i < users.size(); i++) {
                    Map<String, String> user = users.get(i);
                    System.out.printf("%d. %s %s\n", i+1, user.get("first_name"), user.get("last_name"));
                }

                selectedUsers.clear();
                String[] tokens = s.nextLine().trim().split(" ");

                // Nothing entered... (the user just hit the enter key)
                if(tokens.length == 1 && tokens[0].isEmpty()) {
                    continue;
                }

                for(String token : tokens) {
                    try {
                        validator.setValue(Integer.parseInt(token));

                        if(validator.isValid()) {
                            int i = (int) validator.getValue() - 1;
                            selectedUsers.add(users.get(i));
                        }
                    }
                    catch(NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                isSelectionValid = selectedUsers.size() == tokens.length;
            } while(!isSelectionValid);

            // Create team

            createNewTeam.setString(1, newTeamName);
            createNewTeam.executeUpdate();

            int newTeamId;

            try(ResultSet rs2 = createNewTeam.getGeneratedKeys()) {
                newTeamId = rs2.getInt(1);
            }

            for(Map<String, String> user : selectedUsers) {
                addUsers.setInt(1, newTeamId);
                addUsers.setString(2, user.get("student_id"));
                addUsers.executeUpdate();
            }

            // // Print information

            System.out.println("\n> Team created\n");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Create team";
    }

}
