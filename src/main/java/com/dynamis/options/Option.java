package com.dynamis.options;

import java.sql.Connection;
import java.sql.SQLException;

public interface Option {
  void run(Connection c) throws SQLException;
}
