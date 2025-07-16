plugins {
    `kotlin-dsl`
}

group = "com.emotionstorage.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("androidAppCompose") {
            id = "com.emotionstorage.convention.application.compose"
            implementationClass = "com.emotionstorage.convention.AndroidAppComposeConventionPlugin"
        }
    }
}