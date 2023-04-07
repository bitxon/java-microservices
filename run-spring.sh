#!/bin/bash
rm spring-app/build/libs/spring-app-1.0-SNAPSHOT.jar
./gradlew build
java -Xms256m -Xmx512m -jar spring-app/build/libs/spring-app-1.0-SNAPSHOT.jar
