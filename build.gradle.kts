import info.solidsoft.gradle.pitest.PitestTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val axonVersion = "4.4.1"
val axonExtensionsVersion = "4.4"
val graphqlKotlinVersion = "3.2.0"
val shazamcrestVersion = "0.11"
val pitestJunit5Version = "0.12"

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.google.cloud.tools.jib") version "2.4.0"
    id("info.solidsoft.pitest") version "1.5.1"
    id("org.sonarqube") version "3.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    jacoco
}

group = "club.piggyplanner"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //Spring boot webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    //Spring actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //Micrometer for prometheus
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    //Mongo DB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    //Kotiln
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //Axon framework
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion")
    implementation("org.axonframework:axon-micrometer:$axonVersion")
    implementation("org.axonframework.extensions.mongo:axon-mongo:$axonExtensionsVersion")

    //GraphQL
    implementation("com.expediagroup:graphql-kotlin-spring-server:$graphqlKotlinVersion")
    implementation("com.expediagroup:graphql-kotlin-schema-generator:$graphqlKotlinVersion")

    //Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.shazam:shazamcrest:$shazamcrestVersion")

    //Mongo db testing
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")

    //Axon framework testing
    testImplementation("org.axonframework:axon-test:$axonVersion")

    //Pitest extension for junit5
    testImplementation("org.pitest:pitest-junit5-plugin:$pitestJunit5Version")
}

jib {
    to {
        image = "pigplanclub/piggyplanner-services"
        tags = setOf(System.getenv("BUILD_VERSION") ?: "$version")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    jacocoTestReport {
        reports {
            xml.isEnabled = true
            xml.destination = File("$buildDir/reports/jacoco/report.xml")
            csv.isEnabled = false
            html.isEnabled = true
        }
        executionData(File("build/jacoco/test.exec"))
    }

    withType<PitestTask> {
        testPlugin.set("junit5")
        threads.set(1)
        outputFormats.set(setOf("HTML"))
        mutators.set(setOf("DEFAULTS"))
        avoidCallsTo.set(setOf("kotlin.jvm.internal", "kotlinx.coroutines"))
//        targetClasses.set(setOf("club.pigplan.piggyplanner.*"))
        targetClasses.set(setOf(
                "club.pigplan.piggyplanner.account.*",
                "club.pigplan.piggyplanner.user.*"
        ))
    }

    named("build") {
        dependsOn("test")
    }
}

jacoco {
    toolVersion = "0.8.5"
}

sonarqube {
    properties {
        property("sonar.projectKey", "pigplan-club_piggyplanner-services")
        property("sonar.organization", "pigplanclub")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", "43d07c88a743309184313a31040f14e96e13ac63")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/report.xml")
    }
}
