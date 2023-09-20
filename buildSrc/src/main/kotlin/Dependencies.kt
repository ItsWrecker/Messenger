object Dependencies {

    object Core {
        const val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
        const val lifecycleKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycleRuntimeKtx}"
    }

    object Compose {
        const val composeActivity =
            "androidx.activity:activity-compose:${Versions.Compose.composeActivity}"
        const val composeBOM = "androidx.compose:compose-bom:${Versions.Compose.composeBOM}"
        const val composeUI = "androidx.compose.ui:ui"
        const val composeGraphics = "androidx.compose.ui:ui-graphics"
        const val composePreview = "androidx.compose.ui:ui-tooling-preview"
        const val composeMaterial = "androidx.compose.material3:material3"
        const val composeNavigation =
            "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.constraintLayout}"
    }

    object UnitTest {
        const val junit = "junit:junit:${Versions.UnitTest.junit}"
    }

    object AndroidTest {
        const val junitExt = "androidx.test.ext:junit:${Versions.AndroidTest.junitExt}"
        const val expressoCore =
            "androidx.test.espresso:espresso-core:${Versions.AndroidTest.espressoCore}"
        const val composeBOM = "androidx.compose:compose-bom:${Versions.Compose.composeBOM}"
        const val composeUI = "androidx.compose.ui:ui-test-junit4"
    }

    object DebugTest {
        const val composeUI = "androidx.compose.ui:ui-tooling"
        const val composeUIManifest = "androidx.compose.ui:ui-test-manifest"
    }

    object Hilt {
        const val hiltCore = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    }
}