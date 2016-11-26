package com.github.marschall.stringdedupparser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Path;

import com.github.marschall.lineparser.Line;
import com.github.marschall.lineparser.LineParser;

public final class StringDeduplicationParser {

  private static final String MARKER = "[GC concurrent-string-deduplication, ";

  public ParseResult parse(Path path, Charset charset) throws IOException {
    LineParser parser = new LineParser();
    parser.forEach(path, charset, (line) -> {
      this.parseLine(line);
    });
    return new ParseResult();
  }

  private void parseLine(Line line) {
    CharSequence content = line.getContent();
    int deduplicationIndex = indexOf(content, MARKER);
    if (deduplicationIndex != -1) {
      int closingIndex = lastIndexOf(content, ']');
      if (closingIndex != -1) {
        CharSequence dedup = content.subSequence(
                deduplicationIndex + MARKER.length(), closingIndex);
        CharSequence saved = betweenAnd(dedup, '(', ')');
        if (saved != null) {
          System.out.println(saved);
        }
      }
    }
  }

  static BigDecimal parseMemory(CharSequence charSequence) {
    return BigDecimal.ZERO;
  }

  static CharSequence betweenAnd(CharSequence charSequence, char start, char end) {
    int startIndex = indexOf(charSequence, start);
    if (startIndex != -1) {
      int endIndex = lastIndexOf(charSequence, end);
      if (endIndex > startIndex) {
        return charSequence.subSequence(startIndex + 1, endIndex);
      }
    }
    return null;
  }

  static int indexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = 0; i < length; ++i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  static int lastIndexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = length - 1; i >= 0; --i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
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
