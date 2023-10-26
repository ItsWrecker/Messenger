

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":service:encrypt"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.dataStore.core)
    implementation(libs.serialization)
    implementation(libs.tink)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}