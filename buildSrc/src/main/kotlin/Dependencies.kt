object Dependencies {

    object Core {
        const val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
        const val lifecycleKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycleRuntimeKtx}"
        const val lifecycleCore =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycleRuntimeKtx}"
        const val service =
            "androidx.lifecycle:lifecycle-service:${Versions.AndroidX.lifecycleRuntimeKtx}"
        const val inject = "javax.inject:javax.inject:1"
        const val viewmodel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecycleRuntimeKtx}"
    }

    object Coroutine {
        const val coroutine =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Coroutine.android}"
        const val coroutineCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutine.core}"
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
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.constraintLayout}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.Compose.hiltNavigation}"
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

    object Local {
        const val dataStore = "androidx.datastore:datastore:${Versions.Local.dataStore}"
    }

    object Collections {
        const val immutable =
            "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.Collections.immutable}"
        const val json =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Collections.json}"
    }

    object Xmpp {
        const val smackAndroid = "org.igniterealtime.smack:smack-android:${Versions.Xmpp.smack}"
        const val smackTcp = "org.igniterealtime.smack:smack-tcp:${Versions.Xmpp.smack}"
        const val smackExt = "org.igniterealtime.smack:smack-extensions:${Versions.Xmpp.smack}"
        const val conversations =
            "eu.siacs.conversations:conversations:${Versions.Xmpp.conversation}"

    }
}