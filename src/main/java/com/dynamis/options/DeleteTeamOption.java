package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.sql.SQLException;

import com.dynamis.App;
import com.dynamis.utils.SQLFileReader;
import com.dynamis.validators.InRange;
import com.dynamis.validators.IntegerValidator;
import com.dynamis.validators.Validator;

public class DeleteTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("delete_team.sql");

        Map<String, Object> selectedTeam = null;

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllTeams = c.prepareStatement(sql.get("select_all_teams"));
            PreparedStatement deleteTeam = c.prepareStatement(sql.get("delete_single_team"));
            ResultSet rs = selectAllTeams.executeQuery()) {

            List<Map<String, Object>> teams = new ArrayList<>();

            while(rs.next()) {
                Map<String, Object> team = new HashMap<>();
                team.put("team_id", rs.getInt("id"));
                team.put("team_name", rs.getString("team_name"));
                teams.add(team);
            }

            // Ask which team to delete

            Validator validator = new InRange(new IntegerValidator(), 1, teams.size());

            do {
                System.out.println("\nRemove:");
                for(int i = 0; i < teams.size(); i++) {
                    Map<String, Object> team = teams.get(i);
                    System.out.printf("%d. %s\n", i+1, (String) team.get("team_name"));
                }

                validator.setValue(s.nextInt());
                s.nextLine(); // consume the previous newline character

            } while(!validator.isValid());

            selectedTeam = teams.get((int) validator.getValue() - 1);

            // Delete it

            deleteTeam.setInt(1, (int) selectedTeam.get("team_id"));
            deleteTeam.executeUpdate();

            // Print information

            System.out.println("\n> Team deleted\n");
        }
    }

    @Override
    public String toString() {
        return "Delete team";
    }

}
