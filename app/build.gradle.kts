import org.jetbrains.kotlin.ir.backend.js.compile
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

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
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            val signingPropsFile = file("../signing.properties")
            if (signingPropsFile.exists()) {
                val signingProps = Properties()
                signingProps.load(FileInputStream(signingPropsFile))
                storeFile = file(signingProps.getProperty("RELEASE_STORE_FILE"))
                storePassword = signingProps.getProperty("RELEASE_STORE_PASSWORD")
                keyAlias = signingProps.getProperty("RELEASE_KEY_ALIAS")
                keyPassword = signingProps.getProperty("RELEASE_KEY_PASSWORD")

            }
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true

        }
        val release by getting {
//            this.isMinifyEnabled = true
            this.isDebuggable = false
            this.isJniDebuggable = false
            applicationIdSuffix = ".release"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            this.signingConfig = signingConfigs.getByName("release")
        }
    }



    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}


dependencies {
    api(project(":features"))
//    api(project(":core"))
    implementation(project(":service:xmpp"))
    implementation(project(":core:design"))
    implementation(project(":core:navigation"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))

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

    compileOnly("com.android.support:multidex:1.0.3")
    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            force("org.objenesis:objenesis:2.6")
        }
    }
}
