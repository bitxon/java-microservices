#!/bin/bash

# Warm Up
for run in {1..2}; do
  curl --location 'http://localhost:8080/accounts' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "email": "mike@mail.com", "firstName": "Mike", "lastName": "Brown", "dateOfBirth": "2000-03-17", "currency": "EUR", "moneyAmount": 78 }'
done

for run in {1..3}; do
  sleep 1
  curl --location 'localhost:8080/accounts/transfers' \
    --header 'Content-Type: application/json' \
    --data '{ "senderId": 1, "recipientId": 2, "moneyAmount": 20 }'
done

# Performance Test
./gradlew :loadtest:gatlingRun --simulation gatling.simulation.AppSimulation
