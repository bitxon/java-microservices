# Dropwizard application

How to start the Dropwizard application
---

1. Run `gradle clean build` to build your application
2. Start application with `java -jar build/libs/dropwizard-app-1.0-SNAPSHOT-all.jar server classpath:config.yml`
3. To check that your application is running enter url `curl http://localhost:8080/accounts`

Note: `classpath:config.yml` supported thanks to `ClasspathOrFileConfigurationSourceProvide`


Health Check
---

To see your application's health enter url `http://localhost:8081/healthcheck`
