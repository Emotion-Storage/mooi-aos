package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationComposeConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        println("*** AndroidAppComposeConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                // apply kotlin android & compose plugins
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")

                extensions.configure<ApplicationExtension> {
                    defaultConfig.targetSdk = ApplicationConfig.targetSdk
                    configureKotlinAndroid(this)
                }
            }
        }
    }
}


internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    // get version catalog
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        compileSdk = ApplicationConfig.compileSdk
        defaultConfig {
            minSdk = ApplicationConfig.minSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        buildFeatures {
            compose = true
        }

        // dependency configuration
        dependencies {
            val core = libs.findBundle("core").get()
            val compose = libs.findBundle("compose").get()
            val composeDebug = libs.findBundle("compose.debug").get()
            val composeTest = libs.findBundle("compose.test").get()
            val test = libs.findBundle("test").get()
            val androidTest = libs.findBundle("android.test").get()

            add("implementation", core)
            add("implementation", compose)
            add("debugImplementation", composeDebug)
            add("androidTestImplementation", composeTest)
            add("testImplementation", test)
            add("androidTestImplementation", androidTest)
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("DEPRECATION")
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}