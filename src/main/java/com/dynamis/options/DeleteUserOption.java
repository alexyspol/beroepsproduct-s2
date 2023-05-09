package com.dynamis.options;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.SQLFile;
import com.dynamis.Team;
import com.dynamis.User;

public class DeleteUserOption implements Option {

    private SQLFile sql;

    public DeleteUserOption() throws IOException {
        sql = new SQLFile("delete_user.sql");
    }

    @Override
    public void run(App app) throws SQLException {
        Connection c = app.getConnection();
        Scanner s = app.getScanner();

        PreparedStatement selectAllUsers = c.prepareStatement(sql.nextStatement());
        PreparedStatement selectTeamCount = c.prepareStatement(sql.nextStatement());
        PreparedStatement deleteUser = c.prepareStatement(sql.nextStatement());
        PreparedStatement deleteTeam = c.prepareStatement(sql.nextStatement());
        PreparedStatement deleteContactInfo = c.prepareStatement(sql.nextStatement());
        
        // Step 1: Get all users

        ResultSet rs = selectAllUsers.executeQuery();
        
        // Convert ResultSet into List<User>

        List<User> users = new ArrayList<>();
        List<Team> teams = new ArrayList<>();

        while(rs.next()) {
            String studentId = rs.getString("student_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            int teamId = rs.getInt("team_id");
            String teamName = rs.getString("team_name");

            User user = new User();
            user.setStudentId(studentId);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            Team team = new Team();
            team.setId(teamId);
            team.setTeamName(teamName);

            users.add(user);
            teams.add(team);
        }

        // Step 2: Ask who the user wants to delete

        System.out.println("\nWho do you want to remove?");
        for(int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("%d. %s %s\n", i+1, user.getFirstName(), user.getLastName());
        }

        int selection = s.nextInt();

        if(!(1 <= selection && selection <= users.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + users.size());
        }

        User selectedUser = users.get(selection - 1);
        Team selectedTeam = teams.get(selection - 1);

        // // Step 3: Get team member count

        selectTeamCount.setInt(1, selectedTeam.getId());
        selectTeamCount.setInt(2, selectedTeam.getId());
        rs = selectTeamCount.executeQuery();

        int numTeamMembers = rs.getInt("num_team_members");

        // // Step 4: Delete the user

        deleteUser.setString(1, selectedUser.getStudentId());
        deleteUser.executeUpdate();

        // // Step 5: Delete the user's contact information

        deleteContactInfo.setString(1, selectedUser.getStudentId());
        deleteContactInfo.executeUpdate();

        // // Step 6: Delete the team if member count is 0

        if(numTeamMembers == 0) {
            deleteTeam.setInt(1, selectedTeam.getId());
            deleteTeam.executeUpdate();
        }

        // // Step 7: Print information

        System.out.printf("""

            > Removed: %s %s (%s)
                Team: %s

            """, selectedUser.getFirstName(), selectedUser.getLastName(), selectedUser.getStudentId(), selectedTeam.getTeamName());
    }

    @Override
    public String toString() {
        return "Delete user";
    }

}
