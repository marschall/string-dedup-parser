package com.github.marschall.stringdedupparser;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.github.marschall.charsequences.CharSequences;
import com.github.marschall.lineparser.Line;
import com.github.marschall.lineparser.LineParser;

/**
 * Parses a string deduplication log.
 */
public final class StringDeduplicationParser {

  private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  private static final String MARKER_JDK8 = "[GC concurrent-string-deduplication, ";

  private static final String MARKER_JDK9 = "Concurrent String Deduplication ";

  enum ParseMode {
    JDK8,
    JDK9,
    UNKNOWN;
  }

  private ParseMode parseMode;

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
      if (l >= 0L) {
        saved += l;
        count += 1;
      }
    }

    ParseResult asResult() {
      return new ParseResult(saved, count);
    }

  }

  StringDeduplicationParser () {
    this.parseMode = ParseMode.UNKNOWN;
  }

  private long parseLine(Line line, Aggregator aggregator) {
    switch (this.parseMode) {
      case JDK8:
        return parseLine8(line, aggregator);
      case JDK9:
        return parseLine9(line, aggregator);
      case UNKNOWN:
        CharSequence content = line.getContent();
        int index8 = CharSequences.indexOf(content, MARKER_JDK8);
        if (index8 != -1) {
          this.parseMode = ParseMode.JDK8;
          return parseLine8(line, aggregator);
        }
        int index9 = CharSequences.indexOf(content, MARKER_JDK9);
        if (index9 != -1) {
          this.parseMode = ParseMode.JDK9;
          return parseLine8(line, aggregator);
        }
    }
    return 0L;
  }

  private static long parseLine8(Line line, Aggregator aggregator) {
    CharSequence content = line.getContent();
    int deduplicationIndex = CharSequences.indexOf(content, MARKER_JDK8);
    if (deduplicationIndex != -1) {
      int closingIndex = CharSequences.lastIndexOf(content, ']');
      if (closingIndex > deduplicationIndex) {
        CharSequence dedup = content.subSequence(deduplicationIndex + MARKER_JDK8.length(), closingIndex);
        CharSequence saved = subSequenceBetween(dedup, '(', ')');
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

  private static long parseLine9(Line line, Aggregator aggregator) {
    CharSequence content = line.getContent();
    int deduplicationIndex = CharSequences.indexOf(content, MARKER_JDK9);
    if (deduplicationIndex != -1) {
      int openingIndex = CharSequences.indexOf(content, '(', deduplicationIndex + MARKER_JDK9.length() + 1);
      if (openingIndex != -1) {
        int closingIndex = CharSequences.indexOf(content, ')', openingIndex + 1);
        if (closingIndex != -1) {
          CharSequence saved = content.subSequence(openingIndex + 1, closingIndex);
          long memory = parseMemory(saved);
          aggregator.add(memory);
        } else {
          LOG.warning("could not find ')' on line: "  + content);
        }

      }
    }
    return 0L;
  }

  static long parseMemory(CharSequence charSequence) {
    int length = charSequence.length();
    if (length < 2) {
      LOG.warning("memory too short: " + charSequence);
    }
    int multiplier = extractMemoryMultiplier(charSequence);
    long memory = 0L;
    for (int i = 0; i < length - 1; ++i) {
      char c = charSequence.charAt(i);
      if (c == '.') {
        // 12.345
        // 300 000
        //  40 000
        //   5 000
        memory = memory * multiplier;
        int minorMultiplier = multiplier / 1024 * 100;
        for (int j = i + 1; j < length - 1; ++j) {
          char decimalChar = charSequence.charAt(j);
          if (decimalChar < '0' || decimalChar > '9') {
            throw new NumberFormatException(charSequence.toString());
          }
          int decimalValue = decimalChar - '0';
          memory += decimalValue * minorMultiplier;
          minorMultiplier /= 10;
        }
        return memory;
      }
      if (c < '0' || c > '9') {
        throw new NumberFormatException(charSequence.toString());
      }
      int value = c - '0';
      memory = memory * 10 + value;
    }
    return memory * multiplier;
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

  static CharSequence subSequenceBetween(CharSequence charSequence, char start, char end) {
    int startIndex = CharSequences.indexOf(charSequence, start);
    if (startIndex != -1) {
      int endIndex = CharSequences.lastIndexOf(charSequence, end);
      if (endIndex > startIndex) {
        return charSequence.subSequence(startIndex + 1, endIndex);
      }
    }
    return null;
  }

}
