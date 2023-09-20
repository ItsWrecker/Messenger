object Modules {

    object App {
        val composeBOM = Dependencies.Compose.composeBOM

        val core = listOf(
            Dependencies.Core.coreKtx,
            Dependencies.Core.lifecycleKtx
        )
        val compose = listOf(
            Dependencies.Compose.composeActivity,
            Dependencies.Compose.composeUI,
            Dependencies.Compose.composeGraphics,
            Dependencies.Compose.composePreview,
            Dependencies.Compose.composeMaterial,
            Dependencies.Compose.composeNavigation,
            Dependencies.Compose.constraintLayout
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
        )
        val unitTest = listOf(
            Dependencies.UnitTest.junit
        )
        val androidTest = listOf(
            Dependencies.AndroidTest.junitExt,
            Dependencies.AndroidTest.expressoCore
        )
    }
}