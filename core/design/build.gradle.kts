plugins {
    id("android.library")
    id("android.library.compose")
    id("android.library.jacoco")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)

    debugImplementation(libs.androidx.customview.poolingcontainer)
    debugImplementation(libs.androidx.lifecycle.viewModelCompose)
    debugImplementation(libs.androidx.savedstate.ktx)
}