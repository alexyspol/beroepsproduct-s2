package com.dynamis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SQLFileReader {

    private static String parent = "./src/main/resources/";

    public static Map<String, String> readSQLFile(String filePath) {
        Map<String, String> sqlStatements = new HashMap<>();
        String currentKey = null;
        StringBuilder currentStatement = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(parent + filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("--")) {
                    // Extract key from comment
                    currentKey = line.trim().substring(2).trim();
                } else if (!line.trim().isEmpty()) {
                    currentStatement.append(line).append(" ");
                    if (line.trim().endsWith(";")) {
                        // End of statement, store in map
                        sqlStatements.put(currentKey, currentStatement.toString().trim());
                        currentStatement = new StringBuilder();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlStatements;
    }

}
