#!/bin/bash
java -Xmx16m -Xms16m -Xss256k -XX:+UseSerialGC \
  --module-path modules/ --module com.github.marschall.stringdedupparser/com.github.marschall.stringdedupparser.Main \
  $1
