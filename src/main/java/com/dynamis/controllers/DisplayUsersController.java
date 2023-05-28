package com.dynamis.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynamis.models.ContactInfo;
import com.dynamis.models.Teams;
import com.dynamis.models.Users;
import com.dynamis.views.DisplayUsersView;

public class DisplayUsersController implements Controller {

    private String name;
    private DisplayUsersView view = new DisplayUsersView();
    private Users users;
    private Teams teams;
    private ContactInfo contacts;

    public DisplayUsersController(String name, String url) {
        this.name = name;
        try {
            Connection connection = DriverManager.getConnection(url);
            this.users = new Users(connection);
            this.teams = new Teams(connection);
            this.contacts = new ContactInfo(connection);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        List<Map<String, Object>> everyone = users.readAll();
        List<Map<String, Object>> allTeams = teams.readAll();
        List<Map<String, Object>> allContacts = contacts.readAll();

        List<Map<String, Object>> merged = new ArrayList<>();

        for (Map<String, Object> person : everyone) {
            String teamId = person.get("team_id").toString();

            for (Map<String, Object> team : allTeams) {
                if (team.get("team_id").toString().equals(teamId)) {
                    Map<String, Object> mergedMap = new HashMap<>(person);
                    mergedMap.putAll(team);

                    for (Map<String, Object> contact : allContacts) {
                        if (contact.get("student_id").toString().equals(person.get("student_id").toString())) {
                            mergedMap.putAll(contact);
                            break;
                        }
                    }

                    merged.add(mergedMap);
                    break;
                }
            }
        }

        view.show(merged);
    }

    @Override public String toString() {
        return name;
    }
  
}