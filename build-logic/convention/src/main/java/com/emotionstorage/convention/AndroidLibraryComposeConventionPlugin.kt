package com.emotionstorage.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidLibraryComposeConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
            project.dependencies {
                add("implementation", libs.findBundle("compose").get())
                add("debugImplementation", libs.findBundle("compose.debug").get())
                add("androidTestImplementation", libs.findBundle("compose.test").get())
            }
        }
    }
}