package com.dynamis.validators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class TeamExistsValidator implements Validator {

    @Override
    public boolean isValid(String teamName) {
        boolean exists = false;

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) FROM teams WHERE team_name = '" + teamName + "'");
            ResultSet rs = stmt.executeQuery()) {

            exists = (rs.getInt(1) > 0);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exists;
    }

}
