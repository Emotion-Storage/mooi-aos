package com.emotionstorage.convention

import com.android.build.api.dsl.LibraryExtension
import com.emotionstorage.helper.ApplicationConfig
import com.emotionstorage.helper.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidLibraryConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<LibraryExtension> {
                @Suppress("DEPRECATION")
                defaultConfig.targetSdk = ApplicationConfig.targetSdk
                configureKotlinAndroid(this)
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findBundle("orbit-mvi").get())
            }
        }
    }
}