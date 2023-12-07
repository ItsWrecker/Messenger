pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://www.jitpack.io") {
            content {
                includeModule("com.github.UnifiedPush", "android-connector")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io") {
            content {
                includeModule("com.github.UnifiedPush", "android-connector")
                includeModule("com.github.UnifiedPush", "android-embedded_fcm_distributor")
            }
        }
    }
}
rootProject.name = "Messenger"
include(":app")

include(":features")
include(":features:auth")
include(":features:settings")
include(":features:router")
include(":features:conversations")
include(":features:contacts")
include(":features:chat")

include(":core")
include(":core:common")
include(":core:data")
include(":core:datastore")
include(":core:database")
include(":core:design")
include(":core:model")
include(":core:navigation")
include(":core:ui")
include(":core:xmpp")
include(":service")
include(":service:xmpp")
include(":features:lock")
