pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.dokka") {
                useVersion("1.4.32")
            }
        }
    }

}
rootProject.name = "TypeTrainerMultiplatform"

include(":android")
include(":desktop")
include(":common")
include(":tehras-charts")
include(":treemap")
