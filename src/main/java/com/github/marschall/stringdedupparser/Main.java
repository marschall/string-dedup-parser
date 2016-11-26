package com.github.marschall.stringdedupparser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("usage: logfile");
      System.exit(1);
    }

    Path path = Paths.get(args[0]);
    if (!Files.exists(path)) {
      System.err.println("File " + args[0] + " does not exist");
      System.exit(1);
    }

    StringDeduplicationParser parser = new StringDeduplicationParser();
    ParseResult result = parser.parse(path, StandardCharsets.US_ASCII);
    System.out.println(result);
  }

}
