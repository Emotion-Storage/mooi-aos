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
        create("androidApplication") {
            id = "com.emotionstorage.convention.application.android"
            implementationClass = "com.emotionstorage.convention.AndroidApplicationConventionPlugin"
        }
        create("androidLibrary") {
            id = "com.emotionstorage.convention.library.android"
            implementationClass = "com.emotionstorage.convention.AndroidLibraryConventionPlugin"
        }
        create("javaKotlinLibrary"){
            id = "com.emotionstorage.convention.library.kotlin"
            implementationClass = "com.emotionstorage.convention.JavaKotlinLibraryConventionPlugin"
        }

        create("androidCompose"){
            id = "com.emotionstorage.convention.compose.android"
            implementationClass = "com.emotionstorage.convention.AndroidComposeConventionPlugin"
        }
        create("androidHilt"){
            id = "com.emotionstorage.convention.hilt.android"
            implementationClass = "com.emotionstorage.convention.AndroidHiltConventionPlugin"
        }
        create("hilt"){
            id = "com.emotionstorage.convention.hilt"
            implementationClass = "com.emotionstorage.convention.HiltConventionPlugin"
        }
    }
}