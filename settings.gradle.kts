pluginManagement {
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
