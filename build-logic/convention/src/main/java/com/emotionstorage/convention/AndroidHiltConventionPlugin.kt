package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.impldep.kotlinx.metadata.Flag
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationHiltConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")

                extensions.findByType<ApplicationExtension>()?.let {
                    configureAndroidHilt(this)
                }

                extensions.findByType<LibraryExtension>()?.let {
                    configureAndroidHilt(this)
                }
            }
        }
    }
}

internal fun Project.configureAndroidHilt(extension: Any) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    extension.apply {
        dependencies {
            add("implementation", libs.findBundle("hilt-android").get())
            add("ksp", libs.findLibrary("google-dagger-hilt-compiler").get())
        }
    }
}