plugins {
    id("com.emotionstorage.convention.application.android")
    id("com.emotionstorage.convention.compose.android")
    id("com.emotionstorage.convention.hilt.android")
}

android {
    namespace = "com.emotionstorage.emotionstorage"

    defaultConfig {
        applicationId = "com.emotionstorage.emotionstorage"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // set dynamic features in base module(app)'s build.gradle.kts
    // https://developer.android.com/guide/playcore/feature-delivery?hl=ko#base_feature_relationship
    setDynamicFeatures(
        setOf(
            ":core:ui",
            ":core:local",
            ":core:remote",
            ":feat:tutorial",
            ":feat:auth",
            ":feat:home",
            ":feat:ai-chat",
            ":feat:time-capsule-detail",
            ":feat:time-capsule",
            ":feat:weekly-report",
            ":feat:my",
            ":feat:alarm",
        )
    )
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.feat.tutorial)
    implementation(projects.feat.auth)
    implementation(projects.feat.home)
    implementation(projects.feat.aiChat)
    implementation(projects.feat.timeCapsuleDetail)
    implementation(projects.feat.timeCapsule)
    implementation(projects.feat.weeklyReport)
    implementation(projects.feat.my)
    implementation(projects.feat.alarm)
}