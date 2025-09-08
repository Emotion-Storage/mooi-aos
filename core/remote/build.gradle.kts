import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalProperty(propertyKey: String): String {
    return  System.getenv(propertyKey) ?: gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.hilt")
    id("com.emotionstorage.convention.kotlin.library.retrofit")
}

android {
    namespace = "com.emotionstorage.remote"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "MOOI_DEV_SERVER_URL",
            getLocalProperty("MOOI_DEV_SERVER_URL")
        )
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.domain)
    api(projects.data)
    api(projects.core.common)
}