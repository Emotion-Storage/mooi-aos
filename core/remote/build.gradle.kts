plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.hilt")
}

android {
    namespace = "com.emotionstorage.remote"

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
    api(projects.domain)
    api(projects.data)
    api(projects.core.common)
}