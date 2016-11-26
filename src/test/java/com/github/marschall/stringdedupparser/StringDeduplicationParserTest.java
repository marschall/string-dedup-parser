package com.github.marschall.stringdedupparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class StringDeduplicationParserTest {

  private StringDeduplicationParser parser;

  @Before
  public void setUp() {
    this.parser = new StringDeduplicationParser();
  }

  @Test
  public void parseMiddlewareLog() throws IOException {
    Path path = Paths.get("src", "test", "resources", "middleware.log");
    ParseResult result = this.parser.parse(path, StandardCharsets.US_ASCII);
    assertNotNull(result);
  }

  @Test
  public void indexOf() {
    assertEquals(-1, StringDeduplicationParser.indexOf("abcd", "abcde"));
    assertEquals(-1, StringDeduplicationParser.indexOf("abcd", "bcde"));

    assertEquals(2, StringDeduplicationParser.indexOf("aaab", "ab"));
  }

}
