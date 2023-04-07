#!/bin/bash
rm quarkus-app/build/quarkus-app-1.0-SNAPSHOT-runner.jar
./gradlew build
java -Xms256m -Xmx512m -jar quarkus-app/build/quarkus-app-1.0-SNAPSHOT-runner.jar
