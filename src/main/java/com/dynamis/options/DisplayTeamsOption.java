package com.dynamis.options;

import java.sql.Statement;

import com.dynamis.SqlFileReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayTeamsOption implements Option {

  @Override
  public void run(Connection c) throws SQLException, IOException {
    
    Statement s = c.createStatement();
    ResultSet rs = s.executeQuery(SqlFileReader.read("src/main/resources/sqlite/display_teams.sql"));

    String currentTeam = "";
    int i = 1;

    while(rs.next()) {
      String teamName = rs.getString("team_name");
      String fullName = rs.getString("full_name");

      if(!teamName.equals(currentTeam)) {
        System.out.println();
        
        System.out.println("> " + i + ". " + teamName);
        currentTeam = teamName;
      }

      System.out.println("    " + fullName);
      i++;
    }

    System.out.println();
  }

}
