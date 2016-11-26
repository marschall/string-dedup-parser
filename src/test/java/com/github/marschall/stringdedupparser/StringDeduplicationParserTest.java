package com.github.marschall.stringdedupparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    System.out.println(result);
  }

  @Test
  public void indexOf() {
    assertEquals(-1, StringDeduplicationParser.indexOf("abcd", "abcde"));
    assertEquals(-1, StringDeduplicationParser.indexOf("abcd", "bcde"));
    assertEquals(2, StringDeduplicationParser.indexOf("aaab", "ab"));
  }

  @Test
  public void indexOfChar() {
    assertEquals(2, StringDeduplicationParser.indexOf("aab", 'b'));
    assertEquals(0, StringDeduplicationParser.indexOf("baa", 'b'));
    assertEquals(-1, StringDeduplicationParser.indexOf("aaa", 'b'));
  }

  @Test
  public void lastIndexOf() {
    assertEquals(-1, StringDeduplicationParser.lastIndexOf("aaa", 'b'));
    assertEquals(0, StringDeduplicationParser.lastIndexOf("baa", 'b'));
    assertEquals(2, StringDeduplicationParser.lastIndexOf("aab", 'b'));
  }

  @Test
  public void betweenAnd() {
    assertEquals("20.5K", StringDeduplicationParser.betweenAnd("116.9K->96.4K(20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertEquals("", StringDeduplicationParser.betweenAnd("116.9K->96.4K(), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertNull("20.5K", StringDeduplicationParser.betweenAnd("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(StringDeduplicationParser.betweenAnd("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(StringDeduplicationParser.betweenAnd("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

  @Test
  public void parseMemory() {
    assertEquals((long) (20.5d * 1024), StringDeduplicationParser.parseMemory("20.5K"));
    assertEquals(4808, StringDeduplicationParser.parseMemory("4808.0B"));
    assertEquals(11 * 1024 * 1024, StringDeduplicationParser.parseMemory("11.0M"));
  }

}
