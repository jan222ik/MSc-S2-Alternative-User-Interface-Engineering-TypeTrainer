import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.0-build150"
}

group = "com.github.jan222ik"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
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
            }
        }
        val jvmTest by getting
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
        }
    }
}
