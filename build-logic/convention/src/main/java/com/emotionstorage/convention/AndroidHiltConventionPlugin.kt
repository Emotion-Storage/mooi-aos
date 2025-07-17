package com.emotionstorage.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationHiltConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                // apply ksp & hilt plugins
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")

                extensions.configure<ApplicationExtension> {
                    configureAndroidHilt(this)
                }
            }
        }
    }
}

internal fun Project.configureAndroidHilt(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        dependencies {
            add("implementation", libs.findBundle("hilt-android").get())
            add("ksp", libs.findLibrary("google-dagger-hilt-compiler").get())
        }
    }
}