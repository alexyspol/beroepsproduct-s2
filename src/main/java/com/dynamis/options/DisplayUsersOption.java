package com.dynamis.options;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dynamis.AgeCalculator;

public class DisplayUsersOption implements Option {

  @Override
  public void run(Connection c) throws SQLException {
    System.out.println();

    Statement s = c.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM users");

    while(rs.next()) {
      String studentId = rs.getString("student_id");
      String firstName = rs.getString("first_name");
      String lastName = rs.getString("last_name");
      String dob = rs.getString("dob");
      int teamId = rs.getInt("team_id");
    
      PreparedStatement ps = c.prepareStatement("SELECT team_name FROM teams WHERE id = ?");
      ps.setInt(1, teamId);
      String teamName = ps.executeQuery().getString("team_name");

      System.out.println(
        "> " + studentId + ". " + firstName + " " + lastName + "\n" +
        "    Date of Birth: " + dob + "\n" +
        "    Age: " + AgeCalculator.calculate(dob) + '\n' +
        "    Team: " + teamName + '\n'
      );
    }
  }

}
