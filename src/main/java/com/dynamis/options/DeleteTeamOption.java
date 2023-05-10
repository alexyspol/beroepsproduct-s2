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
import com.dynamis.SQLFile;

public class DeleteTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        SQLFile sql = new SQLFile("delete_team.sql");

        Map<String, Object> selectedTeam = null;
        List<Map<String, Object>> teamMembers = new ArrayList<>();

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllTeams = c.prepareStatement(sql.nextStatement());
            PreparedStatement selectTeamMembers = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteSelectedTeam = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteTeamMembers = c.prepareStatement(sql.nextStatement());
            PreparedStatement deleteContactInfo = c.prepareStatement(sql.nextStatement());
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

            System.out.println("\nRemove:");
            for(int i = 0; i < teams.size(); i++) {
                Map<String, Object> team = teams.get(i);
                System.out.printf("%d. %s\n", i+1, (String) team.get("team_name"));
            }

            int selection = s.nextInt();
            s.nextLine(); // consume the previous newline character

            if(!(1 <= selection && selection <= teams.size())) {
                throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.size());
            }
    
            selectedTeam = teams.get(selection - 1);
            
            selectTeamMembers.setInt(1, (int) selectedTeam.get("team_id"));
            
            try(ResultSet rs2 = selectTeamMembers.executeQuery()) {
                while(rs2.next()) {
                    Map<String, Object> teamMember = new HashMap<>();
                    teamMember.put("student_id", rs2.getString("student_id"));
                    teamMember.put("first_name", rs2.getString("first_name"));
                    teamMember.put("last_name", rs2.getString("last_name"));
    
                    teamMembers.add(teamMember);
                }
            }

            deleteSelectedTeam.setInt(1, (int) selectedTeam.get("team_id"));
            deleteSelectedTeam.executeUpdate();

            deleteTeamMembers.setInt(1, (int) selectedTeam.get("team_id"));
            deleteTeamMembers.executeUpdate();

            for(Map<String, Object> member : teamMembers) {
                deleteContactInfo.setString(1, (String) member.get("student_id"));
                deleteContactInfo.executeUpdate();
            }
        }

        System.out.printf("\n> Deleted team: %s\n", selectedTeam.get("team_name"));
        for(Map<String, Object> member : teamMembers) {
            System.out.printf("    %s %s (%s)", member.get("first_name"), member.get("last_name"), member.get("student_id"));
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Delete team";
    }

}
