package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.emotionstorage.helper.ApplicationConfig
import com.emotionstorage.helper.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure



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

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = ApplicationConfig.targetSdk
                configureKotlinAndroid(this)
            }
        }
    }
}

