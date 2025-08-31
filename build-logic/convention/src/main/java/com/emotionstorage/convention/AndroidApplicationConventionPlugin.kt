package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.emotionstorage.helper.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal object ApplicationConfig {
    val compileSdk = 35
    val minSdk = 27
    val targetSdk = 37
}

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = ApplicationConfig.targetSdk
                configureKotlinAndroid(this)
            }
        }
    }
}

