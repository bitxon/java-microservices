# Java Microservices

- [Dropwizard](dropwizard-app/README.md)
- [Spring](spring-app/README.md)
- [Micronaut](micronaut-app/README.md)
- [Quarkus](quarkus-app/README.md)

# Description
This project is a comparison of Java frameworks.
All applications implement the same functionality and expose the same REST api

# Quick start

1. Build
    - `./gradlew clean build`
2. Spin Up Postgres & Wiremock:
    - `docker-compose up -d`
3. Start one of the Apps:
    - `./run-dropwizard.sh`
    - `./run-spring.sh`
    - `./run-micronaut.sh`
    - `./run-quarkus.sh`
4. Run Gatling:
    - `./gradlew :loadtest:gatlingRun-gatling.simulation.AppSimulation`

<details>
 <summary>Check wiremock performance</summary>


1. Spin Up Wiremock:
   - `docker-compose up -d wiremock`
4. Run Gatling:
   - `./gradlew :loadtest:gatlingRun-gatling.simulation.WiremockSimulation`

</details>



# Rest API

<details>
 <summary><code>GET</code> <code><b>/accounts</b></code> - Get all accounts</summary>

Request Body:
```
N/A
```

Response Body:
```json
[
   {
      "id": 1,
      "email": "john@mail.com",
      "firstName": "John",
      "lastName": "Doe",
      "dateOfBirth": "2000-03-17",
      "currency": "USD",
      "moneyAmount": 78
   }
]
```

</details>

<details>
 <summary><code>GET</code> <code><b>/accounts/{id}</b></code> - Get account by ID</summary>

Request Body:
```
N/A
```

Response Body:
```json
{
   "id": 1,
   "email": "john@mail.com",
   "firstName": "John",
   "lastName": "Doe",
   "dateOfBirth": "2000-03-17",
   "currency": "USD",
   "moneyAmount": 78
}
```

</details>

<details>
 <summary><code>POST</code> <code><b>/accounts</b></code> - Create new account</summary>

Request Body:
```json
{
   "email": "john@mail.com",
   "firstName": "John",
   "lastName": "Doe",
   "dateOfBirth": "2000-03-17",
   "currency": "USD",
   "moneyAmount": 78
}
```

Response Body:
```json
{
   "id": 1,
   "email": "john@mail.com",
   "firstName": "John",
   "lastName": "Doe",
   "dateOfBirth": "2000-03-17",
   "currency": "USD",
   "moneyAmount": 78
}
```

</details>

<details>
 <summary><code>POST</code> <code><b>/accounts/transfer</b></code> - Transfer money from one account to another</summary>

Request Header: (is being used to fail operation and check that transaction works)
```
Dirty-Trick-Header: FAIL_TRANSFER
```

Request Body:
```json
{
   "senderId": 1,
   "recipientId": 2,
   "moneyAmount": 100
}
```

Response Body:
```
N/A
```

</details>

# Comparison

|                              | Quarkus | Micronaut | Dropwizard | Spring                                     |
|------------------------------|---------|-----------|------------|--------------------------------------------|
| Memory consumption on start  | 41 Mb   | 65 Mb     | 90 Mb      | 160 Mb                                     |
| Memory allocated after GC    | 18 Mb   | 18 Mb     | 21 Mb      | 25 Mb                                      |
| Min -Xmx to start app        | 9 Mb    | 17 Mb     | 17 Mb      | 17 Mb                                      |
| Best start time              | 0.836 s | 0.973 s   | 1.574 s    | 2.325 s                                    |
| Jar size                     | 38 Mb   | 33 Mb     | 35 Mb      | 41 Mb                                      |
| # questions on stackoverflow | 6k      | 3k        | 5k         | 200k                                       |
| # GitHub stars               | 10k     | 5k        | 8k         | 62k                                        |
| Based on server              | Netty   | Netty     | Jetty      | Tomcat<br/> Jetty<br/> Netty<br/> Undertow |


# TODO - add performance comparison table and images from VisualVm
