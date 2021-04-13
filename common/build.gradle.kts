import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.30"
    id("org.jetbrains.compose")
}

group = "com.github.jan222ik"
version = "1.0"

repositories {
    google()
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.1.0")
            }
        }
        named("androidMain") {
            kotlin.srcDirs("src/jvmMain/kotlin")
            dependencies {
                api("androidx.appcompat:appcompat:1.3.0-beta01")
                api("androidx.core:core-ktx:1.3.1")
            }
        }
        named("desktopMain") {
            kotlin.srcDirs("src/jvmMain/kotlin")
            resources.srcDirs("src/commonMain/resources")
            dependencies {
                api(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
}
