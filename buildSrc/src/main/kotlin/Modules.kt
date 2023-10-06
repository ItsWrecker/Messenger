object Modules {

    object App {
        val composeBOM = Dependencies.Compose.composeBOM

        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.lifecycleKtx,
            Dependencies.Coroutine.coroutine
        )
        val compose = listOf(
            Dependencies.Compose.composeActivity,
            Dependencies.Compose.composeUI,
            Dependencies.Compose.composeGraphics,
            Dependencies.Compose.composePreview,
            Dependencies.Compose.composeMaterial,
            Dependencies.Compose.composeNavigation,
            Dependencies.Compose.constraintLayout,
            Dependencies.Compose.hiltNavigation
        )

        val unitTest = listOf(
            Dependencies.UnitTest.junit
        )
        val androidTest = listOf(
            Dependencies.AndroidTest.junitExt,
            Dependencies.AndroidTest.expressoCore,
            Dependencies.AndroidTest.composeUI
        )
        val debugTest = listOf(
            Dependencies.DebugTest.composeUI,
            Dependencies.DebugTest.composeUIManifest
        )

        val libs = listOf(
            Dependencies.Hilt.hiltCore
        )
        val kapts = listOf(
            Dependencies.Hilt.hiltCompiler
        )

    }

    object Data {
        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.inject
        )

        val local = listOf(
            Dependencies.Local.dataStore
        )
        val collections = listOf(
            Dependencies.Collections.immutable,
            Dependencies.Collections.json
        )

        val unitTest = listOf(
            Dependencies.UnitTest.junit
        )
        val androidTest = listOf(
            Dependencies.AndroidTest.junitExt,
            Dependencies.AndroidTest.expressoCore
        )
        val xmpp = listOf(
//            Dependencies.Xmpp.conversations,
            Dependencies.Xmpp.smackAndroid,
            Dependencies.Xmpp.smackExt,
            Dependencies.Xmpp.smackTcp,
        )

        val libs = listOf(
            Dependencies.Hilt.hiltCore
        )
        val kapt = listOf(
            Dependencies.Hilt.hiltCompiler
        )
    }

    object Presentation {
        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Coroutine.coroutine,
            Dependencies.Core.viewmodel,
            Dependencies.Core.lifecycleKtx,
            Dependencies.Core.lifecycleCore,
            Dependencies.Core.service
        )
        val libs = listOf(
            Dependencies.Hilt.hiltCore
        )
        val kapt = listOf(
            Dependencies.Hilt.hiltCompiler
        )
        val unitTest = listOf(
            Dependencies.UnitTest.junit
        )
        val androidTest = listOf(
            Dependencies.AndroidTest.junitExt,
            Dependencies.AndroidTest.expressoCore
        )
        val xmpp = listOf(
//            Dependencies.Xmpp.conversations,
            Dependencies.Xmpp.smackAndroid,
            Dependencies.Xmpp.smackExt,
            Dependencies.Xmpp.smackTcp,
        )
    }

    object Domain {
        val core = listOf(
            Dependencies.Coroutine.coroutine,
            Dependencies.Core.inject,
            Dependencies.Collections.json
        )
    }
}