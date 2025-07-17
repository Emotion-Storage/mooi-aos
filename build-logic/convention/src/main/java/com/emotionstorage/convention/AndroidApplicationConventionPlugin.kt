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

internal object ApplicationConfig{
    val compileSdk = 35
    val minSdk = 27
    val targetSdk = 37
}

class AndroidApplicationConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        println("*** AndroidApplicationConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")

                extensions.configure<ApplicationExtension> {
                    defaultConfig.targetSdk = ApplicationConfig.targetSdk
                    configureKotlinAndroid(this)
                }
            }
        }
    }
}


internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        compileSdk = ApplicationConfig.compileSdk
        defaultConfig {
            minSdk = ApplicationConfig.minSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        dependencies {
            add("implementation", libs.findBundle("core").get())
            add("testImplementation", libs.findBundle("test").get())
            add("androidTestImplementation",  libs.findBundle("android.test").get())
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("DEPRECATION")
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
}