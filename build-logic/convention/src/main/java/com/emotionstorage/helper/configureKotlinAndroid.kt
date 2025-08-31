package com.emotionstorage.helper

import com.android.build.api.dsl.CommonExtension
import com.emotionstorage.convention.ApplicationConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = ApplicationConfig.compileSdk
        defaultConfig {
            minSdk = ApplicationConfig.minSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("DEPRECATION")
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add("implementation", libs.findBundle("core").get())
        add("implementation", libs.findLibrary("kotlinx.serialization.json").get())

        add("testImplementation", libs.findBundle("test").get())
        add("androidTestImplementation", libs.findBundle("android.test").get())
    }
}