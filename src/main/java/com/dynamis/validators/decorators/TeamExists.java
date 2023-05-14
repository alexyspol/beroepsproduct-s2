package com.dynamis.validators.decorators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dynamis.validators.IValidator;

public class TeamExists extends BaseValidatorDecorator {

    public TeamExists(IValidator validator) {
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

        return validator.isValid() && exists;
    }

}
