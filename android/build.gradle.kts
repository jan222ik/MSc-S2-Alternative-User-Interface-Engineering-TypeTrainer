plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "1.4.30"
    id("org.jetbrains.dokka")
}

repositories {
    google()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(project(":common"))
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.activity:activity-compose:1.3.0-alpha05")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // MediaPipe deps
    val flogger_version = "0.3.1"
    val guava_version = "27.0.1-android"
    implementation("com.google.flogger:flogger:$flogger_version")
    implementation("com.google.flogger:flogger-system-backend:$flogger_version")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.google.guava:guava:$guava_version")
    implementation("com.google.protobuf:protobuf-java:3.11.4")

    // CameraX core library
    val camerax_version = "1.1.0-alpha03"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")

    // networking
    val ktor_version = "1.5.3"
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.1.0")
    implementation("org.jmdns:jmdns:3.5.6")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.github.jan222ik.TypeTrainerMultiplatform"
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType(org.jetbrains.dokka.gradle.DokkaTask::class).configureEach {
    suppressInheritedMembers.set(true)
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
        }
    }
}

tasks.withType(org.jetbrains.dokka.gradle.DokkaTaskPartial::class).configureEach {
    suppressInheritedMembers.set(true)
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
        }
    }
}
