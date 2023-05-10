package com.dynamis.options;

import com.dynamis.App;
import com.dynamis.SQLFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DisplayTeamsOption implements Option {

    @Override
    public void run(App app) {

        SQLFile sql = new SQLFile("display_teams.sql");

        // Step 1: Fetch the information from the database

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql.nextStatement())) {

            String currentTeam = "";
            int i = 1;
    
            // Step 2: Print it

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
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Display list of teams";
    }

}
