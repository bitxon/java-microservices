# Java Microservices
- [Dropwizard](dropwizard-app/README.md)
- [Spring](spring-app/README.md)

## Quick start
1. Build: `./gradlew clean build`
2. Spin Up DB And Wiremock: `docker-compose -f docker-compose.yml up -d`
3. Start one App: `./run-dropwizard.sh` or `./run-spring.sh`
4. Run Gatling: `./gradlew :loadtest:gatlingRun`
