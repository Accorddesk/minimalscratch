plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    id("org.jetbrains.kotlin.kapt") version "1.3.72"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("application")
}

version = "0.1"
group = "com.accorddesk"

repositories {
    mavenCentral()
    jcenter()
}

val micronautLatest: String by project
val kotlinLatest: String by project


val developmentOnly by configurations.creating

dependencies {
    kapt(platform("io.micronaut:micronaut-bom:${micronautLatest}"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    implementation(platform("io.micronaut:micronaut-bom:${micronautLatest}"))
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-validation")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinLatest}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinLatest}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    developmentOnly("io.micronaut:micronaut-runtime-osx:${micronautLatest}")
    developmentOnly("net.java.dev.jna:jna")
    developmentOnly("io.methvin:directory-watcher")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    kaptTest(enforcedPlatform("io.micronaut:micronaut-bom:${micronautLatest}"))
    kaptTest("io.micronaut:micronaut-inject-java")
    kaptTest("io.micronaut:micronaut-validation")
    testImplementation(enforcedPlatform("io.micronaut:micronaut-bom:${micronautLatest}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

//    // exposed RDBMS access
//    implementation("org.jetbrains.exposed:exposed-core:${jetbrainsExposedLatest}")
////    implementation("org.jetbrains.exposed:exposed-dao:${jetbrainsExposedLatest}")
//    implementation("org.jetbrains.exposed:exposed-java-time:${jetbrainsExposedLatest}")
//    implementation("org.jetbrains.exposed:exposed-jdbc:${jetbrainsExposedLatest}")
//
//    // RDBMS
//    //implementation("com.h2database:h2:${h2JdbcLatest}")
//    implementation("org.postgresql:postgresql:${postgresJdbcLatest}")
//
//    // utils
//    implementation("org.apache.poi:poi:${apachePoiLatest}")
//    implementation("org.apache.poi:poi-ooxml:${apachePoiLatest}")
}

application {
    mainClassName = "com.accorddesk.MainApplication" // Kt" // if main not inside a class/object
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    // val developmentOnly by configurations.creating
    classpath += developmentOnly
    useJUnitPlatform()
}

tasks {
    allOpen {
        annotation("io.micronaut.aop.Around")
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = java.targetCompatibility.majorVersion
            //Will retain parameter names for Java reflection
            javaParameters = true
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = java.targetCompatibility.majorVersion
            javaParameters = true
        }
    }
    kapt {
        arguments {
            arg("micronaut.processing.incremental", false) // true)
            arg("micronaut.processing.annotations", "com.accorddesk.*")
            arg("micronaut.processing.group", "com.accorddesk")
            arg("micronaut.processing.module", "com.accorddesk")
        }
    }

    shadowJar {
        mergeServiceFiles()
    }
}

tasks.withType<JavaExec> {
    classpath += developmentOnly
    jvmArgs("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
    if (gradle.startParameter.isContinuous) {
        systemProperties(
            "micronaut.io.watch.restart" to "true",
            "micronaut.io.watch.enabled" to "true",
            "micronaut.io.watch.paths" to "src/main"
        )
    }
}

tasks.withType<Test> {
    // val developmentOnly by configurations.existing
    classpath += developmentOnly

    useJUnitPlatform {
        // includeTags "fast"
        // excludeTags "app", "integration", "messaging", "slow", "trivial"
    }
    failFast = false
    ignoreFailures = true
    // reports.html.isEnabled = true

    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED) //, STARTED //, standardOut, standardError)
    }

    addTestListener(object : org.gradle.api.tasks.testing.TestListener {
        override fun beforeTest(descriptor: org.gradle.api.tasks.testing.TestDescriptor?) {
            logger.lifecycle("Running " + descriptor)
        }
        override fun beforeSuite(p0: org.gradle.api.tasks.testing.TestDescriptor?) = Unit
        override fun afterTest(desc: org.gradle.api.tasks.testing.TestDescriptor, result: org.gradle.api.tasks.testing.TestResult) = Unit
        override fun afterSuite(desc: org.gradle.api.tasks.testing.TestDescriptor, result: org.gradle.api.tasks.testing.TestResult) {
            if (desc.parent == null) { // will match the outermost suite
                println("\nTotal Test Results:")
                println("===================")
                println("Test Results: ${result.resultType} (total: ${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)\n")
            }
        }
    })

    // listen to standard out and standard error of the test JVM(s)
    // onOutput { descriptor, event -> logger.lifecycle("Test: " + descriptor + " produced standard out/err: " + event.message ) }
}
