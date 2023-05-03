package com.dynamis.options;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisplayUsersOption implements Option {

  @Override
  public void run(Connection c) throws SQLException {
    Statement s = c.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM users");
  
    while(rs.next()) {
      String studentId = rs.getString("student_id");
      String firstName = rs.getString("first_name");
      String lastName = rs.getString("last_name");
      String dob = rs.getString("dob");
      int teamId = rs.getInt("team_id");
    
      String teamName = s.executeQuery("SELECT team_name FROM teams WHERE id = " + teamId).getString("team_name");

      System.out.println(
        studentId + ". " + firstName + " " + lastName + "\n" +
        "  Date of Birth: " + dob + "\n" +
        "  Team: " + teamName
      );
    }
  }

}
