String Deduplication Log Parser
===============================

A parser for HotSpot string deduplication logs.

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

