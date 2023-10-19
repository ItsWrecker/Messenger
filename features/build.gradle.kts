plugins {
    id("android.library")
    id("android.feature")
    id("android.library.compose")
    id("android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}


dependencies {
    api(project(":features:router"))
    api(project(":features:auth"))
    api(project(":features:conversations"))
    api(project(":features:chat"))
    api(project(":features:contacts"))
    api(project(":features:settings"))
}