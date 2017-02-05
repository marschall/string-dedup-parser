String Deduplication Log Parser [![Build Status](https://travis-ci.org/marschall/string-dedup-parser.svg?branch=master)](https://travis-ci.org/marschall/string-dedup-parser) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/string-dedup-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/string-dedup-parser)
===============================

A parser for [HotSpot string deduplication](http://openjdk.java.net/jeps/192) logs. Tested with JDK 8 and JDK 9.

Usage
-----

Download [string-dedup-parser-0.2.2.jar](https://search.maven.org/remotecontent?filepath=com/github/marschall/string-dedup-parser/0.2.2/string-dedup-parser-0.2.2.jar)

Run the JAR with log file to parse as the first command line argument

```
java -jar string-dedup-parser-0.2.2.jar src/test/resources/middleware.log
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

