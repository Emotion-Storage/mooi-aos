plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.compose")
}

android {
    namespace = "com.emotionstorage.ui"

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
}