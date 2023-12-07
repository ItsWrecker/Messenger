plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}