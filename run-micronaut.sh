#!/bin/bash
rm micronaut-app/build/libs/micronaut-app-1.0-SNAPSHOT-all.jar
./gradlew build
java -Xms256m -Xmx512m -jar micronaut-app/build/libs/micronaut-app-1.0-SNAPSHOT-all.jar
