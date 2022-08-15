#!/bin/bash
java -Xms256m -Xmx512m -jar dropwizard-app/build/libs/dropwizard-app-1.0-SNAPSHOT-all.jar server classpath:config.yml
