plugins {
    id("com.emotionstorage.convention.android.application")
    id("com.emotionstorage.convention.android.application.compose")
    id("com.emotionstorage.convention.android.application.hilt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.emotionstorage.emotionstorage"

    defaultConfig {
        applicationId = "com.emotionstorage.emotionstorage"
        versionCode = 1
        versionName = "v0.0.0-beta"

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
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.feat.tutorial)
    implementation(projects.feat.auth)
    implementation(projects.feat.home)
    implementation(projects.feat.aiChat)
    implementation(projects.feat.timeCapsuleDetail)
    implementation(projects.feat.timeCapsule)
    implementation(projects.feat.weeklyReport)
    implementation(projects.feat.my)
    implementation(projects.feat.alarm)
    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))
}