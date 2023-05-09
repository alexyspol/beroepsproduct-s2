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

public class EditTeamOption implements Option {

    private SQLFile sql;

    public EditTeamOption() throws IOException {
        sql = new SQLFile("edit_team.sql");
    }

    @Override
    public void run(App app) throws SQLException {
        Connection c = app.getConnection();
        Scanner s = app.getScanner();

        PreparedStatement selectAllTeams = c.prepareStatement(sql.nextStatement());
        PreparedStatement updateTeamName = c.prepareStatement(sql.nextStatement());

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

        int selection = s.nextInt();
        s.nextLine(); // consume the previous newline character

        if(!(1 <= selection && selection <= teams.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.size());
        }

        Team selectedTeam = teams.get(selection - 1);

        // Step 3: Ask for the new name

        System.out.printf("\nChange \"%s\" to: ", selectedTeam.getTeamName());
        String newTeamName = s.nextLine();

        // Step 4: Change the team name in the database

        updateTeamName.setString(1, newTeamName);
        updateTeamName.setInt(2, selectedTeam.getId());
        updateTeamName.executeUpdate();

        // Step 5: Print it

        System.out.printf("\n> Succesfully changed \"%s\" to \"%s\"\n\n", selectedTeam.getTeamName(), newTeamName);
    }

    @Override
    public String toString() {
        return "Edit team";
    }

}
