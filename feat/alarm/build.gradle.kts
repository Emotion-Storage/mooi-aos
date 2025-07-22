plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.compose")
    id("com.emotionstorage.convention.android.library.hilt")
}

android {
    namespace = "com.emotionstorage.alarm"

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
    implementation(projects.domain)
    implementation(projects.core.common)
    implementation(projects.core.ui)
}