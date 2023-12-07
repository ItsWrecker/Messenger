plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}


dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    implementation(libs.smack.tcp)
    api(libs.smack.android)
    api(libs.smack.android.extensions)
    api(libs.smack.omemo)
    api(libs.smack.omemo.signal)
    api(libs.smack.sasl.provided)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(platform("com.google.firebase:firebase-bom:30.0.0"))
    api("com.google.firebase:firebase-analytics")
    api("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.guava:guava:31.1-android")
    api("com.github.UnifiedPush:android-connector:2.0.0")
    implementation("com.github.UnifiedPush:android-embedded_fcm_distributor:2.2.0")
    implementation("com.google.crypto.tink:apps-webpush:1.9.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // smack xpp3 exclusion
    configurations {
        all {
            exclude(group = "xpp3", module = "xpp3")
        }
    }
}