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
    assertEquals(148L, result.getCount());
    assertEquals(169928456L, result.getSaved());
  }

  @Test
  public void parseMiddlewareJdk9Log() throws IOException {
    Path path = Paths.get("src", "test", "resources", "middleware-jdk9.log");
    ParseResult result = this.parser.parse(path, StandardCharsets.US_ASCII);
    assertNotNull(result);
    assertEquals(19L, result.getCount());
    assertEquals(980892L, result.getSaved());
  }

  @Test
  public void subSequenceBetween() {
    assertEquals("20.5K", StringDeduplicationParser.subSequenceBetween("116.9K->96.4K(20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertEquals("", StringDeduplicationParser.subSequenceBetween("116.9K->96.4K(), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertNull("20.5K", StringDeduplicationParser.subSequenceBetween("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(StringDeduplicationParser.subSequenceBetween("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(StringDeduplicationParser.subSequenceBetween("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

  @Test
  public void parseMemory() {
    assertEquals(1L, StringDeduplicationParser.parseMemory("1B"));
    assertEquals(1024L, StringDeduplicationParser.parseMemory("1K"));
    assertEquals((1024 * 1024) + (1024 * 100), StringDeduplicationParser.parseMemory("1.1M"));
    assertEquals((1024 * 1024) + (1024 * 230), StringDeduplicationParser.parseMemory("1.23M"));
    assertEquals((20 * 1024) + 500, StringDeduplicationParser.parseMemory("20.5K"));
    assertEquals(4808, StringDeduplicationParser.parseMemory("4808.0B"));
    assertEquals(11 * 1024 * 1024, StringDeduplicationParser.parseMemory("11.0M"));
  }

}
