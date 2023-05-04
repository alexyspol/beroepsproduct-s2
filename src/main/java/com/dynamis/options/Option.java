package com.dynamis.options;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface Option {
  void run(Connection c) throws SQLException, IOException;
}
