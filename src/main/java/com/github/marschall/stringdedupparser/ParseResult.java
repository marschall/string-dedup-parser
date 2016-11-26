package com.github.marschall.stringdedupparser;

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

}
