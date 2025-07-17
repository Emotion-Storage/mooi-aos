plugins {
    id("com.emotionstorage.convention.application.compose")
}

android {
    namespace = "com.emotionstorage.emotionstorage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.emotionstorage.emotionstorage"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.bundles.core)

    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.test)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.android.test)

    implementation(project(":domain"))
}