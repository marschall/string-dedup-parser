package com.github.marschall.stringdedupparser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import com.github.marschall.lineparser.Line;
import com.github.marschall.lineparser.LineParser;

public final class StringDeduplicationParser {

  public ParseResult parse(Path path, Charset charset) throws IOException {
    LineParser parser = new LineParser();
    parser.forEach(path, charset, (line) -> {
      this.parseLine(line);
    });
    return new ParseResult();
  }

  private void parseLine(Line line) {
    CharSequence content = line.getContent();
    int deduplicationIndex = indexOf(content, "[GC concurrent-string-deduplication, ");
    if (deduplicationIndex != -1) {
      System.out.println(content);
    }
  }

  static int indexOf(CharSequence charSequence, String subSequence) {
    int sequenceLength = charSequence.length();
    int subSequenceLength = subSequence.length();
    charLoop : for (int i = 0; i <= sequenceLength - subSequenceLength; ++i) {
      for (int j = 0; j < subSequenceLength; ++j) {
        if (charSequence.charAt(i + j) != subSequence.charAt(j)) {
          continue charLoop;
        }
      }
      return i;
    }
    return -1;
  }

}
