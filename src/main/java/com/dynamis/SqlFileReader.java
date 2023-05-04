package com.dynamis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SqlFileReader {
  
  public static String read(String filename) throws IOException {
    String content = new String(Files.readAllBytes(Paths.get(filename)));
    return content;
  }

}
