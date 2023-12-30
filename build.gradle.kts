plugins {
    kotlin("jvm") version "1.9.21" apply false
}

repositories {
    mavenCentral()
}

subprojects {

    tasks.withType<Test> {
        useJUnitPlatform {
            excludeTags("excluded")
        }
        reports {
            junitXml.apply {
                isOutputPerTestCase = true // defaults to false
            }
        }
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events("passed", "skipped", "failed")
        }
    }
}

tasks.withType(Wrapper::class) {
    gradleVersion = "8.5"
}