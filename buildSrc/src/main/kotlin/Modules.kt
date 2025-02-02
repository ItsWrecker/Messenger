object Modules {

    object App {
        val composeBOM = Dependencies.Compose.composeBOM

        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.lifecycleKtx,
            Dependencies.Coroutine.coroutine,
            Dependencies.Kotlin.dateTime
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
        val xmpp = listOf(
            Dependencies.Xmpp.smackAndroid,
            Dependencies.Xmpp.smackExt,
            Dependencies.Xmpp.smackTcp,
        )

    }

    object Data {
        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.inject
        )

        val local = listOf(
            Dependencies.Local.dataStore,
            Dependencies.Local.roomCore,
            Dependencies.Local.roomKtx
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

        val kotlin = listOf(
            Dependencies.Kotlin.dateTime
        )

        val libs = listOf(
            Dependencies.Hilt.hiltCore
        )
        val kapt = listOf(
            Dependencies.Hilt.hiltCompiler,
            Dependencies.Local.roomCompiler
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
            Dependencies.Hilt.hiltCore,
            Dependencies.Kotlin.dateTime
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
            Dependencies.Collections.json,
            Dependencies.Kotlin.dateTime
        )
    }

    object Common {
        val libs = listOf(
            Dependencies.Kotlin.dateTime,
            Dependencies.Coroutine.coroutine,
            Dependencies.Hilt.hiltCore
        )
        val kapt = listOf(
            Dependencies.Hilt.hiltCompiler
        )
    }

    object Model {
        val libs = listOf(
            Dependencies.Kotlin.dateTime
        )
    }

    object DesignSystem {
        val compose = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.lifecycleKtx,
            Dependencies.Compose.composeUI,
            Dependencies.Compose.composeGraphics,
        )
    }
}