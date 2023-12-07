buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.7.0")
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
        classpath("com.google.gms:google-services:4.3.15")

    }
}
plugins {
//    id("com.google.gms.google-services") version "4.4.0" apply false
}

