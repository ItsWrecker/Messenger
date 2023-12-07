object Config {

    const val namespace = "com.qxlabai.messenger"
    object Android {
        const val androidMinSdkVersion = 28
        const val androidTargetVersion = 33
        const val androidCompileSdkVersion = 34
        const val androidBuildToolVersion = ""
    }

    object Plugins {
        const val android = "com.android.application"
        const val kotlin = "org.jetbrains.kotlin.android"
        const val kapt = "kapt"
        const val hilt = "com.google.dagger.hilt.android"
        const val androidLibrary = "com.android.library"
        const val javaLibrary = "java-library"
        const val kotlinLibrary = "org.jetbrains.kotlin.jvm"
        const val serialization = "org.jetbrains.kotlin.plugin.serialization"
    }


    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}