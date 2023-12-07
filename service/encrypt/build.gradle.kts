plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

}

dependencies {


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // smack xpp3 exclusion
    configurations {
        all {
            exclude(group = "xpp3", module = "xpp3")
        }
    }
}