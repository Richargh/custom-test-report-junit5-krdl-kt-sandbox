plugins {
    kotlin("jvm")
    id ("java-test-fixtures")
}

val kotlinVersion = "1.9.21"
val assertjVersion = "3.24.2"

kotlin {
    jvmToolchain(17)
}

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

tasks.test {
    ignoreFailures = true
    useJUnitPlatform()
}

dependencies {
    testImplementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testFixturesImplementation(kotlin("reflect"))
    testFixturesImplementation(platform("org.junit:junit-bom:5.10.1"))
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-params")
    testFixturesImplementation("org.assertj:assertj-core:$assertjVersion")
}

repositories {
    mavenCentral()
}
