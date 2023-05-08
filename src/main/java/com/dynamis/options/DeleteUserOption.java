package com.dynamis.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.Team;
import com.dynamis.User;

public class DeleteUserOption implements Option {

    private String[] sql = {
        """
            SELECT u.student_id, u.first_name, u.last_name, u.team_id,
                   t.team_name
            FROM users u, teams t
            WHERE u.team_id = t.id;
        """,
        """
            SELECT COUNT(u.student_id) AS num_team_members,
                   t.team_name
            FROM users u, teams t
            WHERE u.team_id = ? AND t.id = ?;
        """,
        """
            DELETE FROM users
            WHERE student_id = ?;
        """,
        """
            DELETE FROM teams
            WHERE id = ?;
        """,
        """
            DELETE FROM contact_info
            WHERE student_id = ?;
        """
    };

    @Override
    public void run(App app) throws SQLException {
        Connection c = app.getConnection();
        Scanner s = app.getScanner();
        
        PreparedStatement selectAllUsers = c.prepareStatement(sql[0]);
        PreparedStatement teamCountAndName = c.prepareStatement(sql[1]);
        PreparedStatement deleteUser = c.prepareStatement(sql[2]);
        PreparedStatement deleteTeam = c.prepareStatement(sql[3]);
        PreparedStatement deleteContactInfo = c.prepareStatement(sql[4]);
        
        // Step 1: Get all users

        ResultSet rs = selectAllUsers.executeQuery();
        
        // Convert ResultSet into List<User>

        List<User> users = new ArrayList<>();
        
        while(rs.next()) {
            String studentId = rs.getString("student_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            int teamId = rs.getInt("team_id");

            User user = new User();
            user.setStudentId(studentId);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            Team team = new Team();
            team.setId(teamId);
            user.setTeam(team);

            users.add(user);
        }

        // Step 2: Ask who the user wants to delete

        System.out.println("\nWho do you want to remove?");
        for(int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("%d. %s %s\n", i+1, user.getFirstName(), user.getLastName());
        }

        int userInput = s.nextInt();

        if(!(1 <= userInput && userInput <= users.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + users.size());
        }

        User selectedUser = users.get(userInput - 1);

        // Step 3: Get team information

        teamCountAndName.setInt(1, selectedUser.getTeam().getId());
        teamCountAndName.setInt(2, selectedUser.getTeam().getId());
        rs = teamCountAndName.executeQuery();

        int numTeamMembers = rs.getInt("num_team_members");
        String teamName = rs.getString("team_name");
        selectedUser.getTeam().setTeamName(teamName);

        // Step 4: Delete the user

        deleteUser.setString(1, selectedUser.getStudentId());
        deleteUser.executeUpdate();

        // Step 5: Delete the user's contact information

        deleteContactInfo.setString(1, selectedUser.getStudentId());
        deleteContactInfo.executeUpdate();

        // Step 6: Delete the team if member count is 0

        if(numTeamMembers == 0) {
            deleteTeam.setInt(1, selectedUser.getTeam().getId());
            deleteTeam.executeUpdate();
        }

        // Step 7: Print information

        System.out.printf("""

            > Removed: %s %s (%s)
                Team: %s

            """, selectedUser.getFirstName(), selectedUser.getLastName(), selectedUser.getStudentId(), selectedUser.getTeam().getTeamName());
    }

    @Override
    public String toString() {
        return "Delete user";
    }

}
