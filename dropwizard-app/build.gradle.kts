plugins {
    java
    application
    id("com.gradleup.shadow") version "9.2.1"
}

group = "bitxon.dropwizard"
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
    implementation(enforcedPlatform("io.dropwizard:dropwizard-bom:5.0.0"))
    implementation("io.dropwizard:dropwizard-core")
    implementation("io.dropwizard:dropwizard-client")
    implementation("io.dropwizard:dropwizard-validation")
    implementation("io.dropwizard:dropwizard-hibernate")
    implementation("org.mapstruct:mapstruct:1.6.3")

    annotationProcessor("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    compileOnly("org.projectlombok:lombok:1.18.42")

    runtimeOnly("org.postgresql:postgresql:42.7.8")

    testImplementation("io.dropwizard:dropwizard-testing")
    testImplementation(project(":common-wiremock"))
    testImplementation(platform("org.testcontainers:testcontainers-bom:2.0.2"))
    testImplementation("org.wiremock:wiremock:3.13.2")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("bitxon.dropwizard.DropwizardApplication")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get(),
            "Build-Jdk-Spec" to java.sourceCompatibility,
            "Class-Path" to sourceSets.main.get().runtimeClasspath.files.joinToString(" ") { it.name }
        )
    }
}

tasks.shadowJar {
    mergeServiceFiles()
    exclude("META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.SF")
}

tasks.named<JavaExec>("run") {
    args = listOf("server", "classpath:config.yml")
}
