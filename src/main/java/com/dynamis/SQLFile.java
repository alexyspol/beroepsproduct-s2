package com.dynamis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SQLFile {

    private static String parent = "./src/main/resources/";

    private String[] statements;
    private int index = -1;

    public SQLFile(String filename) {
        try {
            statements = readFile(filename).split(";");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String nextStatement() {
        index++;
        return statements[index];
    }

    private String readFile(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(parent + filename)));
        return content;
    }
}
