package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.emotionstorage.helper.ApplicationConfig
import com.emotionstorage.helper.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType


class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.google.gms.google-services")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("napier").get())
                add("implementation", libs.findBundle("orbit-mvi").get())

            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = ApplicationConfig.targetSdk
                configureKotlinAndroid(this)
            }
        }
    }
}

