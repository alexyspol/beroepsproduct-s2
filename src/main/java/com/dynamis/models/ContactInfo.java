package com.dynamis.models;

import java.sql.Connection;

public class ContactInfo extends Model {

    public ContactInfo(Connection connection) {
        super(connection);
    }

    @Override public String getTableName() {
        return "contact_info";
    }

    @Override public String getPrimaryKey() {
        return "student_id";
    }

}
