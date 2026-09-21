plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.25.0"
}

group = "eu.bilch"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-oxm")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp") // RabbitMQ
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.22.2")
    implementation("io.github.threeten-jaxb:threeten-jaxb-core:2.2.0")
    // Jackson für JSON
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.22.2")
    implementation("org.openapitools:jackson-databind-nullable:0.2.11")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("io.netty:netty-resolver-dns-native-macos::osx-aarch_64")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// OpenAPI Generator Konfiguration
openApiGenerate {
    generatorName = "java"
    inputSpec = file("Timetables-1.0.274.yaml") // Pfad zu deinem OpenAPI-Dokument
    outputDir = file("${layout.buildDirectory.asFile.get()}/generated-sources/openapi") // Ausgabeverzeichnis
    apiPackage = "eu.bilch.timetables.client.api"
    modelPackage = "eu.bilch.timetables.client.model"
    configOptions = mapOf(
        "dateLibrary" to "java-time", // oder "java-time" für Java 8+ Date/Time
        "library" to "webclient", // Verwende WebClient für Spring 5+
        "useBeanValidation" to "true",
        "useSpringBoot4" to "true",
        "withXml" to "true",
        "reactive" to "true"
    )
}

sourceSets {
    main {
        java {
            srcDir(file("${layout.buildDirectory.asFile.get()}/generated-sources/openapi/src/main/java").path)
        }
    }
}

// Füge das generierte Verzeichnis zum Source-Set hinzu
//tasks.named("compileJava") {
//    dependsOn("openApiGenerate")
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
