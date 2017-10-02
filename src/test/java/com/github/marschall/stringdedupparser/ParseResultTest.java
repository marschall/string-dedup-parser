package com.github.marschall.stringdedupparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ParseResultTest {

  @Test
  public void formatMemory() {
    assertEquals("0.0B", ParseResult.formatMemory(0L));
    assertEquals("1.0B", ParseResult.formatMemory(1L));

    assertEquals("9999.0B", ParseResult.formatMemory(9_999L));

    assertEquals("9.8K", ParseResult.formatMemory(10_000L));
    assertEquals("97.7K", ParseResult.formatMemory(100_000L));
    assertEquals("9765.6K", ParseResult.formatMemory(9_999_999L));

    assertEquals("9.5M", ParseResult.formatMemory(10_000_000L));
    assertEquals("9536.7M", ParseResult.formatMemory(9_999_999_999L));

    assertEquals("9.3G", ParseResult.formatMemory(10_000_000_000L));
    assertEquals("9313.2G", ParseResult.formatMemory(9_999_999_999_999L));

    assertEquals("9.1T", ParseResult.formatMemory(10_000_000_000_000L));
    assertEquals("9094.9T", ParseResult.formatMemory(9_999_999_999_999_999L));
  }

}
