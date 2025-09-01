package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.emotionstorage.helper.configureKotlinAndroid
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.dependencies

internal object ApplicationConfig {
    val compileSdk = 35
    val minSdk = 27
    val targetSdk = 37
    val javaVersion = JavaVersion.VERSION_18
}

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.google.gms.google-services")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = ApplicationConfig.targetSdk
                configureKotlinAndroid(this)
            }
        }
    }
}

