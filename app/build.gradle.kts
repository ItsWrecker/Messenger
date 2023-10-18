plugins {
    id("android.application")
    id("android.application.compose")
    id("android.application.jacoco")
    kotlin("kapt")
    id("jacoco")
    id("dagger.hilt.android.plugin")
}

@Suppress("UnstableApiUsage")
android {
    namespace = Config.namespace

    defaultConfig {
        applicationId = Environments.Debug.appId
        versionCode = Environments.Debug.appVersionCode
        versionName = Environments.Debug.appVersionName

        testInstrumentationRunner = "com.qxlabai.messenger.MessengerTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
        }
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":feature-router"))
    implementation(project(":feature-auth"))
    implementation(project(":feature-conversations"))
    implementation(project(":feature-chat"))
    implementation(project(":feature-contacts"))
    implementation(project(":feature-settings"))
    implementation(project(":service-xmpp"))

    implementation(project(":core-designsystem"))
    implementation(project(":core-navigation"))
    implementation(project(":core-data"))
    implementation(project(":core-model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.accompanist.systemuicontroller)

    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            force("org.objenesis:objenesis:2.6")
        }
    }
}
