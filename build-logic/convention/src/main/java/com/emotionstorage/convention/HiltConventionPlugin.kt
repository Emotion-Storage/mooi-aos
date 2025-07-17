package com.emotionstorage.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** HiltConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                configureHilt()
            }
        }
    }
}

internal fun Project.configureHilt() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add("ksp", libs.findLibrary("google-dagger-hilt-compiler").get())
    }
}