package com.dynamis.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynamis.models.Teams;
import com.dynamis.models.Users;
import com.dynamis.views.DisplayTeamsView;

public class DisplayTeamsController implements Controller {

    private String description;
    private DisplayTeamsView view = new DisplayTeamsView();
    private Users users;
    private Teams teams;

    public DisplayTeamsController(String description, String url) {
        this.description = description;
        try {
            Connection connection = DriverManager.getConnection(url);
            this.users = new Users(connection);
            this.teams = new Teams(connection);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        List<Map<String, Object>> everyone = users.readAll();
        List<Map<String, Object>> allTeams = teams.readAll();

        Map<String, List<String>> teamMembers = new HashMap<>();

        for (Map<String, Object> team : allTeams) {
            String teamId = team.get("team_id").toString();
            String teamName = team.get("team_name").toString();
            List<String> members = new ArrayList<>();

            for (Map<String, Object> person : everyone) {
                if (person.get("team_id").toString().equals(teamId)) {
                    String firstName = person.get("first_name").toString();
                    String lastName = person.get("last_name").toString();
                    members.add(firstName + " " + lastName);
                }
            }

            teamMembers.put(teamName, members);
        }

        view.show(teamMembers);
    }

    @Override public String toString() {
        return description;
    }

}
