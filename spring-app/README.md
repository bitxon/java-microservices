# Dropwizard

How to start the Dropwizard application
---

1. Run `gradle clean build` to build your application
1. Start application with `java -jar build/libs/spring-app-1.0-SNAPSHOT.jar`
1. To check that your application is running enter url `curl http://localhost:8080/accounts`

Health Check
---

To see your application's health enter url `http://localhost:8081/actuator/health`
