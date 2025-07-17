plugins {
    id("com.emotionstorage.convention.library.android")
    id("com.emotionstorage.convention.compose.android")
    id("com.emotionstorage.convention.hilt.android")
}

android {
    namespace = "com.emotionstorage.ai_chat"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
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
    implementation(projects.app)
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.core.common)
}