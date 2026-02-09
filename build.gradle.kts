import com.github.spotbugs.snom.SpotBugsTask

plugins {
    java
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless") version "6.25.0"
    id("checkstyle")
    id("com.github.spotbugs") version "6.0.26"
}

group = "com.example"
version = "0.1.0"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}

spotless {
    java {
        googleJavaFormat("1.23.0")
        target("src/*/java/**/*.java")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.3.1")
    }
    format("misc") {
        target("*.md", ".gitignore", ".editorconfig")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

checkstyle {
    toolVersion = "10.18.2"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
}

tasks.withType<Checkstyle> {
    reports {

            required.set(true)
        }
    }
}

spotbugs {
    toolVersion.set("4.8.6")
}

tasks.withType<SpotBugsTask> {
    reports {

            required.set(true)
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

val bootJar = tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar")
bootJar {
    archiveFileName.set("app.jar")
}
