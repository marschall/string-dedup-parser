package com.github.marschall.stringdedupparser;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.github.marschall.lineparser.Line;
import com.github.marschall.lineparser.LineParser;

public final class StringDeduplicationParser {

  private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  private static final String MARKER = "[GC concurrent-string-deduplication, ";

  public ParseResult parse(Path path, Charset charset) throws IOException {
    LineParser parser = new LineParser();
    Aggregator aggregator = new Aggregator();
    parser.forEach(path, charset, (line) -> {
      this.parseLine(line, aggregator);
    });
    return aggregator.asResult();
  }

  static final class Aggregator {

    private long saved;
    private int count;

    void add(long l) {
      if (l > 0L) {
        saved += l;
        count += 1;
      }
    }

    ParseResult asResult() {
      return new ParseResult(saved, count);
    }

  }

  private long parseLine(Line line, Aggregator aggregator) {
    CharSequence content = line.getContent();
    int deduplicationIndex = indexOf(content, MARKER);
    if (deduplicationIndex != -1) {
      int closingIndex = lastIndexOf(content, ']');
      if (closingIndex > deduplicationIndex) {
        CharSequence dedup = content.subSequence(deduplicationIndex + MARKER.length(), closingIndex);
        CharSequence saved = betweenAnd(dedup, '(', ')');
        if (saved != null) {
          long memory = parseMemory(saved);
          aggregator.add(memory);
        } else {
          LOG.warning("could not find saved memory on line: "  + content);
        }
      } else {
        LOG.warning("could not find ']' on line: "  + content);
      }
    }
    return 0L;
  }

  static long parseMemory(CharSequence charSequence) {
    int length = charSequence.length();
    if (length < 2) {
      LOG.warning("memory too short: " + charSequence);
    }
    BigDecimal memory = new BigDecimal(charSequence.subSequence(0, length - 1).toString());
    int multiplier = extractMemoryMultiplier(charSequence);
    return memory.multiply(BigDecimal.valueOf(multiplier)).longValue();
  }

  private static int extractMemoryMultiplier(CharSequence charSequence) {
    if (charSequence.length() == 0) {
      LOG.warning("empty memory unit");
      return 0;
    }
    char unit = charSequence.charAt(charSequence.length() - 1);
    switch (unit) {
      case 'B':
        return 1;
      case 'K':
        return 1024;
      case 'M':
        return 1024 * 1024;
      case 'G':
        return 1024 * 1024 * 1024;
      default:
        LOG.warning("unknown unit: " + unit);
        return 0;
    }
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
