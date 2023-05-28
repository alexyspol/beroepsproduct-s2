package com.dynamis.controllers;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.models.Teams;
import com.dynamis.views.DeleteTeamView;

public class DeleteTeamController implements Controller {

    private String description;
    private DeleteTeamView view = new DeleteTeamView(new Scanner(new BufferedInputStream(System.in)));
    private Teams teams;

    public DeleteTeamController(String description, String url) {
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
        List<Map<String, Object>> allTeams = teams.readAll();
        int selected = view.pickOne(allTeams);
        Map<String, Object> selectedTeam = allTeams.get(selected);

        teams.delete(selectedTeam.get("team_id"));
        view.success();
    }

    @Override public String toString() {
        return description;
    }
}
