#!/bin/bash
set -e
# mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.1:copy -Dartifact=com.github.marschall:artifact:line-parser:0.4.2 -DoutputDirectory=modules -Dmdep.useBaseVersion=true
# mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.1:copy -Dartifact=com.github.marschall:charsequences:0.4.2        -DoutputDirectory=modules -Dmdep.useBaseVersion=true
cp target/string-dedup-parser-0.3.0-SNAPSHOT.jar modules/string-dedup-parser.jar
cp ~/.m2/repository/com/github/marschall/line-parser/0.4.2/line-parser-0.4.2.jar modules/line-parser.jar
cp ~/.m2/repository/com/github/marschall/charsequences/0.4.2/charsequences-0.4.2.jar modules/charsequences-0.4.2.jar
