plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

group = "com.github.jan222ik"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
