buildscript {
    repositories {
        gradlePluginPortal()
        @Suppress("JcenterRepositoryObsolete") // Required by dokka plugin
        jcenter()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
        //classpath("com.android.tools.build:gradle:4.0.2")

        // __LATEST_COMPOSE_RELEASE_VERSION__
        classpath("org.jetbrains.compose:compose-gradle-plugin:0.4.0-build180")
        classpath("com.android.tools.build:gradle:4.0.1")
        // __KOTLIN_COMPOSE_VERSION__
        classpath(kotlin("gradle-plugin", version = "1.4.32"))

        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
    }
}

group = "com.github.jan222ik"
version = "1.0"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
}

apply(plugin="org.jetbrains.dokka")

tasks.withType(org.jetbrains.dokka.gradle.DokkaMultiModuleTask::class).configureEach {
    outputDirectory.set(buildDir.resolve("dokkaCustomMultiModuleOutput"))
}


tasks.withType(org.jetbrains.dokka.gradle.DokkaTaskPartial::class).configureEach {
    failOnWarning.set(true)
}

abstract class DokkaWithCustomCss : DefaultTask() {
    @TaskAction
    fun run() {}
}

tasks.register<DokkaWithCustomCss>("dokkaHTMLWithCustomCss") {
    dependsOn(":dokkaHtmlMultiModule")
    doLast {
        println("Replace Files")
        delete("build/dokkaCustomMultiModuleOutput/styles/style.css")
        copy {
            duplicatesStrategy = DuplicatesStrategy.WARN
            from("dokka/")
            into("build/dokkaCustomMultiModuleOutput/styles/")
        }
    }
}

