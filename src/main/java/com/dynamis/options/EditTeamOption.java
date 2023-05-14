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
import com.dynamis.SQLFileReader;
import com.dynamis.validators.InRange;
import com.dynamis.validators.IntegerValidator;
import com.dynamis.validators.StringValidator;
import com.dynamis.validators.Validator;
import com.dynamis.validators.UniqueTeamName;

public class EditTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("edit_team.sql");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllTeams = c.prepareStatement(sql.get("select_all_teams"));
            PreparedStatement changeTeamName = c.prepareStatement(sql.get("change_team_name"));
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

            // Select which team to edit

            Validator validator = new InRange(new IntegerValidator(), 1, teams.size());

            do {
                System.out.println("\nEdit team:");
                for(int i = 0; i < teams.size(); i++) {
                    Map<String, Object> team = teams.get(i);
                    System.out.printf("%d. %s\n", i+1, team.get("team_name"));
                }

                validator.setValue(s.nextInt());
                s.nextLine(); // consume the previous newline character

                if(!validator.isValid()) {
                    System.out.println("\n> Your answer needs to be between 1 and " + teams.size());
                }

            } while(!validator.isValid());

            Map<String, Object> selectedTeam = teams.get((int) validator.getValue() - 1);
            System.out.println();

            // Enter new name

            validator = new UniqueTeamName(new StringValidator());

            do {
                System.out.printf("Change team name (%s): ", selectedTeam.get("team_name"));
                validator.setValue(s.nextLine());

            } while(!validator.isValid());

            String newTeamName = (String) validator.getValue();

            if(!newTeamName.isEmpty()) {
                changeTeamName.setString(1, newTeamName);
                changeTeamName.setInt(2, (int) selectedTeam.get("team_id"));
                changeTeamName.executeUpdate();
                
                System.out.println("\n> Team name changed\n");
            }
            else {
                System.out.println("\n> No changes made\n");
            }
        }
    }

    @Override
    public String toString() {
        return "Edit team";
    }

}
