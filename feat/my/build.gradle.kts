import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalProperty(propertyKey: String): String {
    return  System.getenv(propertyKey) ?: gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.compose")
    id("com.emotionstorage.convention.android.library.hilt")
}

android {
    namespace = "com.emotionstorage.my"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "MOOI_REPLY_EMAIL",
            getLocalProperty("MOOI_REPLY_EMAIL")
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
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.ui)
}