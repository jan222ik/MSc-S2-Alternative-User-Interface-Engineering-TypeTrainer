import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    java
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.4.30"
    id("org.jetbrains.dokka")
}

group = "com.github.jan222ik"
version = "1.0.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)

                implementation(kotlin("reflect"))

                // Charts
                implementation(project(":tehras-charts"))
                implementation(project(":treemap"))
                //implementation(project(":staakk-ccharts"))
                implementation(project(":custom-line-charts"))

                // Database
                val exposedVersion = "0.26.2"
                val h2Version = "1.4.200"
                val hikariCpVersion = "3.4.5"
                implementation("com.h2database:h2:$h2Version")
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
                implementation("com.zaxxer:HikariCP:$hikariCpVersion")

                // Networking - Ktor Server & REST & Websocket
                val ktorVersion = "1.3.2"
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-websockets:$ktorVersion")
                implementation("org.jmdns:jmdns:3.5.6")

                // Logger
                val logbackVersion = "1.2.3"
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.1.0")

                // QR
                arrayOf(
                    "core",
                    "swing",
                    "kotlin"//,
                    //"WebcamCapture"
                ).forEach { implementation("org.boofcv:boofcv-$it:0.36") }

                // CSV
                implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "TypeTrainer"
        }
    }
}

tasks.withType(org.jetbrains.dokka.gradle.DokkaTask::class).configureEach {
    suppressInheritedMembers.set(true)
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
            perPackageOption() {
                matchingRegex.set(""".*\_learn.*""")
                suppress.set(true)
            }
        }
    }
}

tasks.withType(org.jetbrains.dokka.gradle.DokkaTaskPartial::class).configureEach {
    suppressInheritedMembers.set(true)
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
            perPackageOption() {
                matchingRegex.set(""".*\_learn.*""")
                suppress.set(true)
            }
        }
    }
}
