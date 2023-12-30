plugins {
    kotlin("jvm")
    id("java-test-fixtures")
}

val kotlinVersion = "1.9.21"
val assertjVersion = "3.24.2"

kotlin {
    jvmToolchain(17)
}

tasks.test {
    ignoreFailures = true
    useJUnitPlatform()
}

dependencies {
    implementation(project(":commons"))
    testImplementation(testFixtures(project(":commons")))
    
    testImplementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

repositories {
    mavenCentral()
}
