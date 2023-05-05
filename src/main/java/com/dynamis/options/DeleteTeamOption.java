package com.dynamis.options;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.sql.SQLException;

import com.dynamis.App;
import com.dynamis.SqlFileReader;

public class DeleteTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException, IOException {
        System.out.println();

        Connection connection = app.getConnection();
        Scanner scanner = app.getScanner();

        String[] sqlStatements = SqlFileReader.read("src/main/resources/sqlite/delete_team.sql").split(";");
        PreparedStatement selectAllTeamsStmt = connection.prepareStatement(sqlStatements[0]);
        PreparedStatement selectedStudentsStmt = connection.prepareStatement(sqlStatements[1]);
        PreparedStatement deleteSelectedTeamStmt = connection.prepareStatement(sqlStatements[2]);
        PreparedStatement deleteSelectedUsersStmt = connection.prepareStatement(sqlStatements[3]);
        PreparedStatement deleteContactInfoStmt = connection.prepareStatement(sqlStatements[4]);

        ResultSet teams = selectAllTeamsStmt.executeQuery();
        Map<Integer, Object[]> teamsMap = new HashMap<>();
        int index = 1;
        
        System.out.println("Which team do you want to delete?");
        while(teams.next()) {

            int teamId = teams.getInt("id");
            String teamName = teams.getString("team_name");

            Object[] team = { teamId, teamName };
            teamsMap.put(index, team);
            
            System.out.println(index + ". " + teamName);

            index++;
        }

        int userInput = scanner.nextInt();

        if(!(1 <= userInput && userInput <= teamsMap.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + teamsMap.size());
        }

        Object[] selectedTeam = teamsMap.get(userInput);
        int selectedTeamId = (int) selectedTeam[0];
        String selectedTeamName = (String) selectedTeam[1];

        selectedStudentsStmt.setInt(1, selectedTeamId);
        Object[][] selectedStudents = convertResultSetToArray(selectedStudentsStmt.executeQuery());

        // Firstly, delete the team

        deleteSelectedTeamStmt.setInt(1, selectedTeamId);
        deleteSelectedTeamStmt.executeUpdate();

        // Secondly, delete all the users associated with that team

        deleteSelectedUsersStmt.setInt(1, selectedTeamId);
        deleteSelectedUsersStmt.executeUpdate();

        // Thirdly, delete the contact information for each of the deleted students
        
        for(Object[] student : selectedStudents) {
            String studentId = (String) student[0];

            deleteContactInfoStmt.setString(1, studentId);
            deleteContactInfoStmt.executeUpdate();
        }

        // Lastly, print information
        
        System.out.println();
        System.out.println("> Deleted team: " + selectedTeamName);
        for(int i = 0; i < selectedStudents.length; i++) {
            Object[] student = selectedStudents[i];
            String studentId = (String) student[0];
            String firstName = (String) student[1];
            String lastName = (String) student[2];

            System.out.println("    " + firstName + " " + lastName + " (" + studentId + ")");
        }
        System.out.println();
    }

    Object[][] convertResultSetToArray(ResultSet rs) throws SQLException {

        // Get number of columns in the ResultSet
        int columnCount = rs.getMetaData().getColumnCount();
    
        // Create an ArrayList to hold the rows of data
        List<Object[]> rows = new ArrayList<>();
    
        // Loop through the ResultSet and add each row to the ArrayList
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            rows.add(row);
        }
    
        // Convert the ArrayList to an array
        Object[][] data = new Object[rows.size()][];
        return rows.toArray(data);
    }
}
