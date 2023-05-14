package com.dynamis.validators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UniqueTeamName extends BaseValidatorDecorator {

    public UniqueTeamName(Validator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        boolean exists = false;

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) FROM teams WHERE team_name = '" + getValue() + "'");
            ResultSet rs = stmt.executeQuery()) {
            exists = (rs.getInt(1) > 0);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return validator.isValid() && !exists;
    }

}
