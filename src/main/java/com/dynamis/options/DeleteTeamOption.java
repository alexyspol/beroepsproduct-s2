package com.dynamis.options;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import com.dynamis.App;
import com.dynamis.SqlFileReader;

public class DeleteTeamOption implements Option {

    @Override
    public void run(App app) throws SQLException, IOException {
        // String[] sqlStatements = SqlFileReader.read("src/main/resources/sqlite/delete_team.sql").split(";");
    
        // Step 1: Ask the user which team they want to delete

        Statement s = app.getConnection().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM teams");

        System.out.println("Which team do you want to delete?");
        while(rs.next()) {
            int id = rs.getInt("id");
            String teamName = rs.getString("team_name");

            System.out.println(id + ". " + teamName);
        }
    }

}
