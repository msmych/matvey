plugins {
    application
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "uk.matvey"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven {
        name = "KitPackages"
        url = uri("https://maven.pkg.github.com/msmych/kit")
        credentials {
            username = "matvey"
            password = project.findProperty("ghPackagesRoToken") as? String ?: System.getenv("GH_PACKAGES_RO_TOKEN")
        }
    }
}

val assertjVersion: String by project
val flywayVersion: String by project
val jasyncVersion: String by project
val junitVersion: String by project
val kitVersion: String by project
val kotlinCssVersion: String by project
val ktorVersion: String by project
val paukVersion: String by project
val postgresVersion: String by project
val slonVersion: String by project

dependencies {
    implementation("com.github.jasync-sql:jasync-postgresql:$jasyncVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("uk.matvey:kit:$kitVersion")
    implementation("uk.matvey:pauk:$paukVersion")

    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
}

application {
    mainClass = "uk.matvey.app.AppKt"
}