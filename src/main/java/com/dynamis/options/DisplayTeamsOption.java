package com.dynamis.options;

import com.dynamis.App;
import com.dynamis.SQLFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DisplayTeamsOption implements Option {

    private SQLFile sql;

    public DisplayTeamsOption() throws IOException {
        sql = new SQLFile("display_teams.sql");
    }

    @Override
    public void run(App app) throws SQLException {
        
        // Step 1: Fetch the information from the database

        Statement s = app.getConnection().createStatement();
        ResultSet rs = s.executeQuery(sql.nextStatement());

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
