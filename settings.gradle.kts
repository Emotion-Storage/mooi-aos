@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EmotionStorage"
include(":app")
include(":domain")
include(":core:ui")
include(":core:remote")
include(":core:local")
include(":core:common")
include(":data")
include(":feat:tutorial")
include(":feat:auth")
include(":feat:home")
include(":feat:ai-chat")
include(":feat:time-capsule")
include(":feat:time-capsule-detail")
include(":feat:weekly-report")
include(":feat:my")
include(":feat:alarm")
