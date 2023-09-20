plugins {
    id(Config.Plugins.android)
    id(Config.Plugins.kotlin)
    kotlin(Config.Plugins.kapt)
    id(Config.Plugins.hilt)
}

android {
    namespace = Config.namespace
    compileSdk = Config.Android.androidCompileSdkVersion

    defaultConfig {
        applicationId = Environments.Debug.appId
        minSdk = Config.Android.androidMinSdkVersion
        targetSdk = Config.Android.androidTargetVersion
        versionCode = Environments.Debug.appVersionCode
        versionName = Environments.Debug.appVersionName

        testInstrumentationRunner = Config.testInstrumentationRunner

        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    Modules.App.composeBOM.let { implementation(platform(it)) }
    Modules.App.core.forEach { implementation(it) }
    Modules.App.compose.forEach { implementation(it) }

    Modules.App.unitTest.forEach { testImplementation(it) }
    Modules.App.androidTest.forEach { androidTestImplementation(it) }
    Modules.App.debugTest.forEach { debugImplementation(it) }

    Modules.App.libs.forEach { implementation(it) }
    Modules.App.kapts.forEach { kapt(it) }

}