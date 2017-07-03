#!/bin/bash
java -Xmx16m -Xms16m -Xss256k -XX:+UseSerialGC \
  -classpath "modules/*" com.github.marschall.stringdedupparser.Main \
  $1
