String Deduplication Log Parser [![Build Status](https://travis-ci.org/marschall/string-dedup-parser.svg?branch=master)](https://travis-ci.org/marschall/string-dedup-parser) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/string-dedup-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/string-dedup-parser)
===============================

A parser for HotSpot string deduplication logs. Tested with JDK 8 and JDK 9.

Usage
-----

Run the JAR with log file to parse as the first command line argument

```
java -jar target/string-dedup-parser-0.1.0-SNAPSHOT.jar src/test/resources/middleware.log
```

the output will look something like this

```
total memory saved 162.1M in 148 deduplications
```

To get the logs you to to run at least with

```
-XX:+UseStringDeduplication -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDetails
```

as string deduplication is only available with G1.

