package com.dynamis.options;

import java.sql.SQLException;

import com.dynamis.App;

public interface Option {
    void run(App app) throws SQLException;
}
