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

public class DeleteUserOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        SQLFile sql = new SQLFile("delete_user.sql");

        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> selectedUser = null;

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllUsers = c.prepareStatement(sql.nextStatement());
            ResultSet rs = selectAllUsers.executeQuery()) {

            while(rs.next()) {
                String studentId = rs.getString("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int teamId = rs.getInt("team_id");
                String teamName = rs.getString("team_name");

                Map<String, Object> user = new HashMap<>();
                user.put("student_id", studentId);
                user.put("first_name", firstName);
                user.put("last_name", lastName);
                user.put("team_id", teamId);
                user.put("team_name", teamName);

                users.add(user);
            }
        }

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectTeamCount = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteUser = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteTeam = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteContactInfo = c.prepareStatement(sql.nextStatement())) {

            System.out.println("\nWho do you want to remove?");
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
        
            selectTeamCount.setInt(1, (int) selectedUser.get("team_id"));
            selectTeamCount.setInt(2, (int) selectedUser.get("team_id"));

            int numTeamMembers;
            try(ResultSet rs = selectTeamCount.executeQuery()) {
                numTeamMembers = rs.getInt("num_team_members");
            }

            deleteUser.setString(1, (String) selectedUser.get("student_id"));
            deleteUser.executeUpdate();

            deleteContactInfo.setString(1, (String) selectedUser.get("student_id"));
            deleteContactInfo.executeUpdate();

            if(numTeamMembers == 0) {
                deleteTeam.setInt(1, (int) selectedUser.get("team_id"));
                deleteTeam.executeUpdate();
            }
        }

        System.out.printf("""

            > Removed: %s %s (%s)
                Team: %s

            """, selectedUser.get("first_name"), selectedUser.get("last_name"), selectedUser.get("student_id"), selectedUser.get("team_name"));
    }

    @Override
    public String toString() {
        return "Delete user";
    }

}
