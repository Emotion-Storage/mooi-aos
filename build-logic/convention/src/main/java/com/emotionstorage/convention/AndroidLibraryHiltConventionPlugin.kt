package com.emotionstorage.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidLibraryHiltConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")

                extensions.findByType<LibraryExtension>()?.let {
                    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

                    it.apply {
                        dependencies {
                            add("implementation", libs.findBundle("hilt-android").get())
                            add("ksp", libs.findLibrary("google-dagger-hilt-compiler").get())
                        }
                    }
                }
            }
        }
    }
}
