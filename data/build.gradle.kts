plugins {
    id(Config.Plugins.androidLibrary)
    id(Config.Plugins.kotlin)

}

android {
    namespace = Config.namespace
    compileSdk = Config.Android.androidCompileSdkVersion

    defaultConfig {
        minSdk = Config.Android.androidMinSdkVersion

        testInstrumentationRunner = Config.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    Modules.Data.core.forEach { implementation(it) }
    Modules.Data.unitTest.forEach { implementation(it) }
    Modules.Data.androidTest.forEach { androidTestImplementation(it) }
}