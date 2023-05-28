package com.dynamis.controllers;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.models.Teams;
import com.dynamis.views.CreateTeamView;

public class CreateTeamController implements Controller {

    private String name;
    private CreateTeamView view = new CreateTeamView(new Scanner(new BufferedInputStream(System.in)));
    private Teams teams;

    public CreateTeamController(String name, String url) {
        this.name = name;
        try {
            Connection connection = DriverManager.getConnection(url);
            this.teams = new Teams(connection);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        List<Map<String, Object>> existingTeams = teams.readAll();

        // Create a String[] to store the team names
        String[] teamNames = new String[existingTeams.size()];

        // Iterate over the list and extract the team_name from each map
        for (int i = 0; i < existingTeams.size(); i++) {
            Map<String, Object> teamMap = existingTeams.get(i);
            String teamName = (String) teamMap.get("team_name");
            teamNames[i] = teamName;
        }

        Map<String, Object> newTeam = new HashMap<>();
        newTeam.put("team_name", view.promptForNewTeam(teamNames));

        teams.create(newTeam);
        view.success();
    }

    @Override public String toString() {
        return name;
    }

}