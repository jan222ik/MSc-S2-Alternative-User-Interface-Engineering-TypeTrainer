import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.2"
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
                api(compose.animation)
                api(compose.foundation)
                api(compose.material)
                api(compose.runtime)
                api(compose.ui)
                api(compose.materialIconsExtended)
                implementation(kotlin("reflect"))

                // Database
                val exposedVersion = "0.26.2"
                val h2Version = "1.4.200"
                val hikariCpVersion = "3.4.5"
                implementation("com.h2database:h2:$h2Version")
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("com.zaxxer:HikariCP:$hikariCpVersion")

                // Ktor Server & REST & Websocket
                val ktorVersion = "1.3.2"
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-websockets:$ktorVersion")

                // Logger
                val logbackVersion = "1.2.3"
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
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
