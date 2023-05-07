package com.dynamis.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

import com.dynamis.App;
import com.dynamis.Team;
import com.dynamis.User;

public class DeleteTeamOption implements Option {
    private String[] sql = {
        """
            SELECT * FROM teams;
        """,
        """
            SELECT student_id, first_name, last_name
            FROM users
            WHERE team_id = ?;
        """,
        """
            DELETE FROM teams WHERE id = ?;
        """,
        """
            DELETE FROM users WHERE team_id = ?;
        """,
        """
            DELETE FROM contact_info WHERE student_id = ?;
        """
    };

    @Override
    public void run(App app) throws SQLException {

        Connection connection = app.getConnection();
        Scanner scanner = app.getScanner();

        PreparedStatement selectAllTeams = connection.prepareStatement(sql[0]);
        PreparedStatement selectUsers = connection.prepareStatement(sql[1]);
        PreparedStatement deleteSelectedTeam = connection.prepareStatement(sql[2]);
        PreparedStatement deleteSelectedUsers = connection.prepareStatement(sql[3]);
        PreparedStatement deleteContactInfo = connection.prepareStatement(sql[4]);
        
        // Step 1: Get all the teams from database
        
        ResultSet rs = selectAllTeams.executeQuery();
        
        // Convert ResultSet into List<Team>

        List<Team> teams = new ArrayList<>();
        
        while(rs.next()) {
            int teamId = rs.getInt("id");
            String teamName = rs.getString("team_name");

            Team team = new Team();
            team.setId(teamId);
            team.setTeamName(teamName);

            teams.add(team);
        }
        
        // Step 2: Ask the user which team needs to be deleted

        System.out.println("\nWhich team do you want to delete?");
        for(int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            String teamName = team.getTeamName();
            System.out.println(i+1 + ". " + teamName);
        }

        int userInput = scanner.nextInt();

        if(!(1 <= userInput && userInput <= teams.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.size());
        }

        // Step 3: Get the team to be deleted

        Team selectedTeam = teams.get(userInput - 1);

        // Step 4: Get the users associated with that team

        selectUsers.setInt(1, selectedTeam.getId());
        rs = selectUsers.executeQuery();

        // Convert ResultSet into List<User>

        List<User> users = new ArrayList<>();

        while(rs.next()) {
            String studentId = rs.getString("student_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");

            User user = new User();
            user.setStudentId(studentId);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            users.add(user);
        }

        // Step 5: Delete the team

        deleteSelectedTeam.setInt(1, selectedTeam.getId());
        deleteSelectedTeam.executeUpdate();

        // Step 6: Delete the users

        deleteSelectedUsers.setInt(1, selectedTeam.getId());
        deleteSelectedUsers.executeUpdate();

        // Step 7: Delete user's contact information

        for(User user : users) {
            String studentId = user.getStudentId();

            deleteContactInfo.setString(1, studentId);
            deleteContactInfo.executeUpdate();
        }

        // Step 8: Print information
        
        System.out.println("\n> Deleted team: " + selectedTeam.getTeamName());

        for(int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String studentId = user.getStudentId();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();

            System.out.printf("    %s %s (%s)\n", firstName, lastName, studentId);
        }
        System.out.println();
    }
}
