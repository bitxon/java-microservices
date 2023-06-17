#!/bin/bash
docker-compose down
docker-compose up -d mockserver

#Warm Up
sleep 10
for run in {1..3}; do
    curl --location 'http://localhost:8888/exchanges?currency=USD'
    curl --location 'http://localhost:8888/exchanges?currency=EUR'
    curl --location 'http://localhost:8888/exchanges?currency=GBP'
done

#Test
sleep 3
./gradlew :loadtest:gatlingRun-gatling.simulation.WiremockSimulation
