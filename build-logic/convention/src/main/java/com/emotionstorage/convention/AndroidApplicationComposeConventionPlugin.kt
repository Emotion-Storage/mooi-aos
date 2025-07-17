package com.emotionstorage.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        println("*** AndroidAppComposeConventionPlugin invoked ***")
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
        }
    }
}