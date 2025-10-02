import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalProperty(propertyKey: String): String {
    return System.getenv(propertyKey) ?: gradleLocalProperties(rootDir, providers).getProperty(
        propertyKey,
    )
}

plugins {
    id("com.emotionstorage.convention.android.application")
    id("com.emotionstorage.convention.android.application.compose")
    id("com.emotionstorage.convention.android.application.hilt")
}

android {
    namespace = "com.emotionstorage.emotionstorage"

    defaultConfig {
        applicationId = "com.emotionstorage.emotionstorage"
        versionCode = 1
        versionName = "0.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders +=
            mapOf(
                "KAKAO_NATIVE_APP_KEY" to
                    getLocalProperty("KAKAO_NATIVE_APP_KEY").replace(
                        "\"",
                        "",
                    ),
            )
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            getLocalProperty("KAKAO_NATIVE_APP_KEY"),
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
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
    implementation(libs.kakao.sdk.user)
}
