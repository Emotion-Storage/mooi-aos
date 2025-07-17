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
            implementationClass = "com.emotionstorage.convention.AndroidApplicationComposeConventionPlugin"
        }
        create("javaKotlinLibrary"){
            id = "com.emotionstorage.convention.library"
            implementationClass = "com.emotionstorage.convention.JavaKotlinLibraryConventionPlugin"
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