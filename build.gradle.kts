buildscript {
    dependencies {
        // Plugin Google Services pour Firebase
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    // Versions Gradle et Kotlin
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
