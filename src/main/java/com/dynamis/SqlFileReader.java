package com.dynamis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SqlFileReader {
  private static String base = "src/main/resources/sqlite/";
  
  public static String read(String filename) throws IOException {
    String content = new String(Files.readAllBytes(Paths.get(base + filename)));
    return content;
  }

}
