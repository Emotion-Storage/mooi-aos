package com.emotionstorage.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidAppComposeConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        println("*** AndroidAppComposeConventionPlugin invoked ***")
        // Additional configuration here...
    }
}