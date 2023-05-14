package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.utils.SQLFileReader;
import com.dynamis.validators.StringValidator;
import com.dynamis.validators.UniqueTeamName;
import com.dynamis.validators.Validator;

public class CreateTeamOption implements Option {

    @Override
    public void run(App app) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("create_team.sql");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllTeams = c.prepareStatement(sql.get("select_all_teams"));
            PreparedStatement createNewTeam = c.prepareStatement(sql.get("create_new_team"));
            ResultSet rs = selectAllTeams.executeQuery()) {

            List<String> teams = new ArrayList<>();

            while(rs.next()) {
                teams.add(rs.getString("team_name"));
            }

            Validator validator = new UniqueTeamName(new StringValidator());

            do {
                System.out.print("Create new team: ");
                validator.setValue(s.nextLine().trim());

            } while(!validator.isValid());

            String newTeamName = (String) validator.getValue();

            createNewTeam.setString(1, newTeamName);
            createNewTeam.executeUpdate();

            // Print information

            System.out.println("\n> Team created\n");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Create team";
    }

}
