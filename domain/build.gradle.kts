plugins {
    id(Config.Plugins.javaLibrary)
    id(Config.Plugins.kotlinLibrary)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    Modules.Domain.core.forEach {
        implementation(it)
    }
}