package com.emotionstorage.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidLibraryRoomConventionPlugin invoked ***")

        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")

                extensions.findByType<LibraryExtension>()?.let {
                    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

                    it.apply {
                        dependencies {
                            add("ksp", libs.findLibrary("androidx-room-compiler").get())
                            add("implementation", libs.findBundle("room").get())
                        }
                    }
                }
            }
        }
    }
}