plugins {
    id(Config.Plugins.androidLibrary)
    id(Config.Plugins.kotlin)
    kotlin(Config.Plugins.kapt)
    id(Config.Plugins.hilt)

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
    implementation(project(":domain"))
    implementation(project(":data"))
    Modules.Presentation.core.forEach { implementation(it) }
    Modules.Presentation.unitTest.forEach { implementation(it) }
    Modules.Presentation.androidTest.forEach { androidTestImplementation(it) }
    Modules.Presentation.libs.forEach { implementation(it) }
    Modules.Presentation.kapt.forEach {
        kapt(it)
    }
    Modules.Presentation.xmpp.forEach { implementation(it) }
}