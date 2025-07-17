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
                // apply kotlin android plugin
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                // apply compose plugin
                apply("org.jetbrains.kotlin.plugin.compose")

                extensions.configure<ApplicationExtension> {
                    defaultConfig.targetSdk = ApplicationConfig.targetSdk
                    configureKotlinAndroid(this)
                    configureAndroidCompose(this)
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
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}


internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            add("implementation", libs.findBundle("compose").get())
            add("debugImplementation", libs.findBundle("compose.debug").get())
            add("androidTestImplementation", libs.findBundle("compose.test").get())
        }
    }
}