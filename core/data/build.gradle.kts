plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}