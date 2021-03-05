plugins {
    id("org.jetbrains.compose") version "0.3.2"
    id("com.android.application")
    kotlin("android")
}

group = "com.github.jan222ik"
version = "1.0"

repositories {
    google()
}

dependencies {
    implementation(project(":common"))
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.github.jan222ik.TypeTrainerMultiplatform"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
