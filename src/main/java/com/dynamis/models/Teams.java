package com.dynamis.models;

import java.sql.Connection;

public class Teams extends Model {

    public Teams(Connection connection) {
        super(connection);
    }

    @Override public String getTableName() {
        return "teams";
    }

    @Override public String getPrimaryKey() {
        return "team_id";
    }

}
