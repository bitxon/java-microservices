# Dropwizard

## Build & Run

1. Build
    - `gradle clean build`
2. Start application
    - `java -jar build/libs/dropwizard-app-1.0-SNAPSHOT-all.jar server classpath:config.yml`
3. To check that your application is running
    - `curl http://localhost:8080/accounts`
    - `curl http://localhost:8081/healthcheck`

Note: `classpath:config.yml` supported via customization `ClasspathOrFileConfigurationSourceProvide`
