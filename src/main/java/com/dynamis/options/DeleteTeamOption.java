package com.dynamis.options;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        PreparedStatement allTeams = connection.prepareStatement(sqlStatements[0]);
        PreparedStatement deleteTeam = connection.prepareStatement(sqlStatements[3]);
        PreparedStatement deleteUsers = connection.prepareStatement(sqlStatements[2]);
        PreparedStatement selectedStudents = connection.prepareStatement(sqlStatements[1]);
        PreparedStatement deleteContactInfo = connection.prepareStatement(sqlStatements[4]);

        Object[][] teams = convertResultSetToArray(allTeams.executeQuery());

        System.out.println("Which team do you want to delete?");

        for(int i = 0; i < teams.length; i++) {
            Object[] currentTeam = teams[i];
            String teamName = (String) currentTeam[1];
        
            System.out.println(i+1 + ". " + teamName);
        }

        int userInput = scanner.nextInt();

        if(!(1 <= userInput && userInput <= teams.length)) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + teams.length);
        }

        int idTeamToBeDeleted = (int) teams[userInput - 1][0];

        selectedStudents.setInt(1, idTeamToBeDeleted);
        Object[][] students = convertResultSetToArray(selectedStudents.executeQuery());

        // Firstly, delete the team

        deleteTeam.setInt(1, idTeamToBeDeleted);
        deleteTeam.executeUpdate();

        // Secondly, delete all the users associated with that team

        deleteUsers.setInt(1, idTeamToBeDeleted);
        deleteUsers.executeUpdate();

        // Lastly, delete the contact information for each of the deleted students
        
        for(Object[] student : students) {
            String studentId = (String) student[0];

            deleteContactInfo.setString(1, studentId);
            deleteContactInfo.executeUpdate();
        }
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
