@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}