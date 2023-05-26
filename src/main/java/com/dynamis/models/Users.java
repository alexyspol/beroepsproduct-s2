package com.dynamis.models;

import java.sql.Connection;

public class Users extends Model {

    public Users(Connection connection) {
        super(connection);
    }

    @Override public String getTableName() {
        return "users";
    }

    @Override public String getPrimaryKey() {
        return "student_id";
    }

}
