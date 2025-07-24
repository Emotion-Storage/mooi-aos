package com.emotionstorage.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.JavaPluginExtension

class JavaKotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** JavaKotlinLibraryConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")

                extensions.getByType<JavaPluginExtension>().apply {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17

                    tasks.withType<KotlinCompile>().configureEach {
                        @Suppress("DEPRECATION")
                        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
                    }
                }
            }
        }
    }
}