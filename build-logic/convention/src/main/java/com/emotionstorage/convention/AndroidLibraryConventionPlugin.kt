package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        println("*** AndroidLibraryConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")

                extensions.configure<LibraryExtension> {
                    @Suppress("DEPRECATION")
                    defaultConfig.targetSdk = ApplicationConfig.targetSdk
                    configureKotlinAndroid(this)
                }
            }
        }
    }
}