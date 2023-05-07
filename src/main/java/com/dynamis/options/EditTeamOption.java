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

public class EditTeamOption implements Option {

    private String[] sql = {
        """
            SELECT * FROM teams;
        """,
        """
            UPDATE teams
            SET team_name = ?
            WHERE id = ?;
        """
    };

    @Override
    public void run(App app) throws SQLException {
        Connection c = app.getConnection();
        Scanner s = app.getScanner();

        PreparedStatement selectAllTeams = c.prepareStatement(sql[0]);
        PreparedStatement updateTeamName = c.prepareStatement(sql[1]);

        // Step 1: Get all teams

        ResultSet rs = selectAllTeams.executeQuery();

        // Convert ResultSet to List<Team>

        List<Team> teams = new ArrayList<>();

        while(rs.next()) {
            int id = rs.getInt("id");
            String teamName = rs.getString("team_name");

            Team team = new Team();
            team.setId(id);
            team.setTeamName(teamName);

            teams.add(team);
        }

        // Step 2: Ask the user which team to change

        System.out.println("\nEdit team:");
        for(int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            System.out.printf("%d. %s\n", i+1, team.getTeamName());
        }

        int userInput = s.nextInt();

        if(!(1 <= userInput && userInput <= teams.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.size());
        }

        Team selectedTeam = teams.get(userInput - 1);

        // Step 3: Ask for the new name

        System.out.printf("\nChange \"%s\" to: ", selectedTeam.getTeamName());

        s.nextLine(); // consume the previous newline character
        String newTeamName = s.nextLine();

        // Step 4: Change the team name in the database

        updateTeamName.setString(1, newTeamName);
        updateTeamName.setInt(2, selectedTeam.getId());
        updateTeamName.executeUpdate();

        // Step 5: Print it

        System.out.printf("\n> Succesfully changed \"%s\" to \"%s\"\n\n", selectedTeam.getTeamName(), newTeamName);
    }

}
