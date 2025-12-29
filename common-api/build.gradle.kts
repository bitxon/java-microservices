plugins {
    `java-library`
    id("io.freefair.lombok") version "9.1.0"
}

group = "bitxon.common"
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
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
}
