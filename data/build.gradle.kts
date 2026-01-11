plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.hilt")

    // add room dependency for TimeCapsuleLocal model
    // need TimeCapsuleModel for PagingSource
    id("com.emotionstorage.convention.android.library.room")
}


android {
    namespace = "com.emotionstorage.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
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

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}


dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)

    implementation(libs.androidx.paging.common)
}
