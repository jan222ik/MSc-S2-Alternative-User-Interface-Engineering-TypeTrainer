import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

group = "com.github.jan222ik.compose-mpp-charts"
version = "1.0"

repositories {
    google()
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
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
                api(compose.ui)
                implementation("org.jetbrains.compose.ui:ui-util:0.4.0-build180")
            }
        }
        named("androidMain") {
            kotlin.srcDirs("src/androidMain/kotlin")
            dependencies {
                api("androidx.appcompat:appcompat:1.3.0-beta01")
                api("androidx.core:core-ktx:1.3.1")
            }
        }
        named("desktopMain") {
            kotlin.srcDirs("src/desktopMain/kotlin")
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
