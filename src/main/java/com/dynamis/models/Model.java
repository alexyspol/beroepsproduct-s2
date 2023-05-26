package com.dynamis.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Model {

    private Connection connection;

    public Model(Connection connection) {
        this.connection = connection;
    }

    public abstract String getTableName();
    public abstract String getPrimaryKey();

    public void create(Map<String, Object> data) {
        String sql = "INSERT INTO " + getTableName() + " (";
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();

            columns.append(column).append(", ");
            values.append("?, ");
            params.add(value);
        }

        // Remove the trailing commas
        columns.delete(columns.length() - 2, columns.length());
        values.delete(values.length() - 2, values.length());

        sql += columns + ") VALUES (" + values + ")";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> read(Object id, String... fields) {
        String query;
        Map<String, Object> result = new HashMap<>();

        if(fields.length == 0) {
            query = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?";
        }
        else {
            query = "SELECT " + String.join(", ", fields) + " FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    result.put(columnName, columnValue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Map<String, Object>> readAll(String... fields) {
        StringBuilder query = new StringBuilder("SELECT ");
        List<Map<String, Object>> result = new ArrayList<>();

        if(fields.length == 0) {
            query.append("* FROM " + getTableName());
        }
        else {
            query.append(String.join(", ", fields) + " FROM " + getTableName());
        }

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query.toString())) {

            while(rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
    
                    row.put(columnName, columnValue);
                }

                result.add(row);
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void update(Object id, Map<String, Object> changes) {
        StringBuilder setClause = new StringBuilder();

        for (String column : changes.keySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(column).append(" = ?");
        }

        try(PreparedStatement statement = connection.prepareStatement("UPDATE " + getTableName() + " SET " + setClause + " WHERE " + getPrimaryKey() + " = ?")) {
            int index = 1;

            for (Object value : changes.values()) {
                statement.setObject(index, value);
                index++;
            }

            statement.setObject(index, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object id) {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?")) {
            statement.setObject(1, id);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
