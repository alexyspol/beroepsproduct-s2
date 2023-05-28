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
import com.dynamis.views.EditTeamView;

public class EditTeamController implements Controller {

    
    private String description;
    private EditTeamView view = new EditTeamView(new Scanner(new BufferedInputStream(System.in)));
    private Teams teams;

    public EditTeamController(String description, String url) {
        this.description = description;
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

        int selected = view.pickOne(existingTeams);
        Map<String, Object> selectedTeam = existingTeams.get(selected);

        String newName = view.promptForNewTeamName((String) selectedTeam.get("team_name"), teamNames);
        
        if(!newName.isEmpty()) {
            Map<String, Object> changes = new HashMap<>();
            changes.put("team_name", newName);

            teams.update(selectedTeam.get("team_id"), changes);
            view.success();
        }
        else {
            view.showNoChanges();
        }
    }

    @Override public String toString() {
        return description;
    }

}
