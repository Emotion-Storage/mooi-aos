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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "EmotionStorage"
include(":app")
include(":domain")
include(":data")
include(":core:ui")
include(":core:remote")
include(":core:local")
include(":core:common")
include(":feat:tutorial")
include(":feat:auth")
include(":feat:home")
include(":feat:ai-chat")
include(":feat:time-capsule")
include(":feat:time-capsule-detail")
include(":feat:weekly-report")
include(":feat:my")
include(":feat:alarm")
