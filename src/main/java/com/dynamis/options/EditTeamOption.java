package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.SQLFile;

public class EditTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        SQLFile sql = new SQLFile("edit_team.sql");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllTeams = c.prepareStatement(sql.nextStatement());
            PreparedStatement changeTeamName = c.prepareStatement(sql.nextStatement());
            ResultSet rs = selectAllTeams.executeQuery()) {

            List<Map<String, Object>> teams = new ArrayList<>();

            while(rs.next()) {
                int teamId = rs.getInt("id");
                String teamName = rs.getString("team_name");
    
                Map<String, Object> team = new HashMap<>();

                team.put("team_id", teamId);
                team.put("team_name", teamName);

                teams.add(team);
            }

            System.out.println("\nEdit team:");
            for(int i = 0; i < teams.size(); i++) {
                Map<String, Object> team = teams.get(i);

                System.out.printf("%d. %s\n", i+1, team.get("team_name"));
            }

            int selection = s.nextInt();
            s.nextLine(); // consume the previous newline character

            if(!(1 <= selection && selection <= teams.size())) {
                throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.size());
            }

            Map<String, Object> selectedTeam = teams.get(selection - 1);

            System.out.printf("\nChange team name (%s): ", selectedTeam.get("team_name"));
            String newTeamName = s.nextLine();
    
            changeTeamName.setString(1, newTeamName);
            changeTeamName.setInt(2, (int) selectedTeam.get("team_id"));
            changeTeamName.executeUpdate();

            System.out.printf("\n> Succesfully changed \"%s\" to \"%s\"\n\n", selectedTeam.get("team_name"), newTeamName);
        }
    }

    @Override
    public String toString() {
        return "Edit team";
    }

}
