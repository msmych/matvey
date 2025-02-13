plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
}

val kitVersion: String by project
val paukVersion: String by project
val coroutinesVersion: String by project
val assertjVersion: String by project
val junitVersion: String by project

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

kotlin {
    jvmToolchain(22)
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

dependencies {
    implementation("uk.matvey:kit:$kitVersion")
    implementation("uk.matvey:pauk:$paukVersion")
    implementation("io.ktor:ktor-client-logging:3.0.3")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
}

tasks.test {
    useJUnitPlatform()
}