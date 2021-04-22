import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    java
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.4.30"
}

group = "com.github.jan222ik"
version = "1.0"


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

                // Database
                val exposedVersion = "0.26.2"
                val h2Version = "1.4.200"
                val hikariCpVersion = "3.4.5"
                implementation("com.h2database:h2:$h2Version")
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("com.zaxxer:HikariCP:$hikariCpVersion")

                // Ktor Server & REST & Websocket
                val ktorVersion = "1.3.2"
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-websockets:$ktorVersion")
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
                ).forEach{ implementation("org.boofcv:boofcv-$it:0.36") }

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
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "jvm"
        }
    }
}
