# Java Microservices

- [Dropwizard](dropwizard-app/README.md)
- [Spring](spring-app/README.md)
- [Micronaut](micronaut-app/README.md)

## Quick start

1. Build
    - `./gradlew clean build`
2. Spin Up Postgres & Wiremock:
    - `docker-compose up -d`
3. Start one of the Apps:
    - `./run-dropwizard.sh`
    - `./run-spring.sh`
    - `./run-micronaut.sh`
4. Run Gatling:
    - `./gradlew :loadtest:gatlingRun`
