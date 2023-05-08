package com.dynamis.options;

import java.sql.Statement;

import com.dynamis.App;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayTeamsOption implements Option {

    private String sql = """
        SELECT t.team_name,
               u.first_name || ' ' || u.last_name AS full_name
        FROM teams t, users u
        WHERE u.team_id = t.id;
        """;

    @Override
    public void run(App app) throws SQLException {
        
        // Step 1: Fetch the information from the database

        Statement s = app.getConnection().createStatement();
        ResultSet rs = s.executeQuery(sql);

        // Step 2: Print it

        String currentTeam = "";
        int i = 1;

        while(rs.next()) {
            String teamName = rs.getString("team_name");
            String fullName = rs.getString("full_name");

            if(!teamName.equals(currentTeam)) {
                System.out.printf("\n> %d. %s\n", i, teamName);
                currentTeam = teamName;
                i++;
            }

            System.out.println("    " + fullName);
        }

        System.out.println();
    }

    @Override
    public String toString() {
        return "Display list of teams";
    }

}
