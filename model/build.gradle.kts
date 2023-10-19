plugins {
    id(Config.Plugins.kotlinLibrary)
}


dependencies {
    Modules.Model.libs.forEach {
        implementation(it)
    }
}