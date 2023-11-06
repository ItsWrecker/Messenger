plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

}


dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    implementation(libs.smack.tcp)
    api(libs.smack.android)
    api(libs.smack.android.extensions)
    api(libs.smack.omemo)
    api(libs.smack.omemo.signal)
    api(libs.smack.sasl.provided)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // smack xpp3 exclusion
    configurations {
        all {
            exclude(group = "xpp3", module = "xpp3")
        }
    }
}