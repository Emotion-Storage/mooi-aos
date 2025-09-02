package com.emotionstorage.convention

import com.emotionstorage.helper.ApplicationConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.dependencies

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** KotlinLibraryConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.getByType<JavaPluginExtension>().apply {
                sourceCompatibility = ApplicationConfig.javaVersion
                targetCompatibility = ApplicationConfig.javaVersion
            }

            tasks.withType<KotlinCompile>().configureEach {
                @Suppress("DEPRECATION")
                kotlinOptions.jvmTarget = ApplicationConfig.javaVersion.toString()
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}