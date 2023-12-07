package com.qxlabai.massenger

import com.android.build.api.dsl.CommonExtension
import com.qxlabai.massenger.FlavorDimension.contentType
import org.gradle.api.Project

enum class FlavorDimension {
    contentType
}

enum class Flavor (val dimension : FlavorDimension, val applicationIdSuffix : String? = null) {
    demo(contentType, ".demo"),
    prod(contentType)
}

fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        flavorDimensions += contentType.name
        productFlavors {
            Flavor.values().forEach{
                create(it.name) {
                    dimension = it.dimension.name
                }
            }
        }
    }
}
