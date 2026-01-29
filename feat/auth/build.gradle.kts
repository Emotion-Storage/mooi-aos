import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalProperty(propertyKey: String) =
    System.getenv(propertyKey) ?: gradleLocalProperties(rootDir, providers).getProperty(propertyKey)

plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.compose")
    id("com.emotionstorage.convention.android.library.hilt")
}

android {
    namespace = "com.emotionstorage.auth"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            getLocalProperty("GOOGLE_SERVER_CLIENT_ID"),
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
    implementation(projects.core.presentation)
    implementation(projects.core.ui)

    implementation(libs.bundles.credentials)
}
