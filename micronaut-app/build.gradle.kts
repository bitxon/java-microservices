plugins {
    id("com.gradleup.shadow") version "9.2.1"
    id("io.micronaut.application") version "4.6.1"
    id("io.micronaut.test-resources") version "4.6.1"
    id("io.micronaut.aot") version "4.6.1"
}

group = "bitxon.micronaut"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common-api"))
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.yaml:snakeyaml")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation(project(":common-wiremock"))
    testImplementation("org.wiremock:wiremock:3.13.2")
    testImplementation("org.eclipse.jetty:jetty-servlet:11.0.26")        // wiremock dependency conflict workaround
    testImplementation("org.eclipse.jetty:jetty-servlets:11.0.26")       // wiremock dependency conflict workaround
    testImplementation("org.eclipse.jetty:jetty-webapp:11.0.26")         // wiremock dependency conflict workaround
    testImplementation("org.eclipse.jetty.http2:http2-server:11.0.26")   // wiremock dependency conflict workaround
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("bitxon.micronaut.MicronautApplication")
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    version("4.10.3")
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("bitxon.micronaut.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
        replaceLogbackXml.set(true)
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}
