package com.github.marschall.stringdedupparser;

import java.text.DecimalFormat;

public final class ParseResult {

  private final long saved;
  private final int count;

  ParseResult(long saved, int count) {
    this.saved = saved;
    this.count = count;
  }

  public long getSaved() {
    return saved;
  }

  public int getCount() {
    return count;
  }

  @Override
  public String toString() {
    return "total memory saved " + formatMemory(saved) + " in " + count + " deduplications";
  }

  static String formatMemory(long bytes) {
    char unit;
    double value;
    if (bytes < 10_000L) {
      unit = 'B';
      value = bytes;
    } else if (bytes < 10_000_000L) {
      unit = 'K';
      value = bytes / 1024.0d;
    } else if (bytes < 10_000_000_000L) {
      unit = 'M';
      value = bytes / (1024.0d * 1024.0d);
    } else if (bytes < 10_000_000_000_000L) {
      unit = 'G';
      value = bytes / (1024.0d * 1024.0d * 1024.0d);
    } else {
      unit = 'T';
      value = bytes / (1024.0d * 1024.0d * 1024.0d * 1024.0d);
    }
    return new DecimalFormat("0.0").format(value) + unit;
  }

}
