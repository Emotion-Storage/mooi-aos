import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getApiKey(propertyKey: String): String {
    return System.getenv(propertyKey) ?: gradleLocalProperties(rootDir, providers).getProperty(
        propertyKey
    )
}

plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.compose")
    id("com.emotionstorage.convention.android.library.hilt")
    id("com.emotionstorage.convention.kotlin.library.retrofit")
}

android {
    namespace = "com.emotionstorage.auth"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            getApiKey("GOOGLE_SERVER_CLIENT_ID")
        )
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            getApiKey("KAKAO_NATIVE_APP_KEY")
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
    implementation(projects.data)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.remote)
    implementation(projects.core.local)

    implementation(libs.bundles.credentials)
    implementation(libs.kakao.sdk.user)
}