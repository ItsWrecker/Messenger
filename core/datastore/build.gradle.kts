

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
//    alias(libs.plugins.protobuf)
    id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

// Setup protobuf configuration
//protobuf {
//    protoc {
//        artifact = libs.protobuf.protoc.get().toString()
//    }
//    generateProtoTasks {
//        all().forEach { task ->
//            task.builtins {
//                val java by registering {
//                    option("lite")
//                }
//                val kotlin by registering {
//                    option("lite")
//                }
//            }
//        }
//    }
//}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":service:encrypt"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.protobuf.kotlin.lite)
//    implementation(libs.serialization)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}