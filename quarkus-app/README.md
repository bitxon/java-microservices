# Quarkus

## Build & Run

1. Build
    - `gradle clean build` (Slim jar)
    - `gradle clean build -Dquarkus.package.type=uber-jar` (Fat Jar)
2. Start application
    - `java -jar quarkus-app/build/quarkus-app/quarkus-run.jar` (Slim jar)
    - `java -jar quarkus-app/build/quarkus-app-1.0-SNAPSHOT-runner.jar` (Fat Jar)
3. To check that your application is running
    - `curl http://localhost:8080/accounts`
    - `curl http://localhost:8080/q/health`

## Run in Dev mode

1. Execute one of:
   - `gradle quarkusDev`
   - `quarkus dev` (Requires Quarkus CLI)
2. Open Dev UI
   - Dev UI at http://localhost:8080/q/dev/


## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/quarkus-app-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.
