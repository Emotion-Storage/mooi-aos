plugins {
    `kotlin-dsl`

    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
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
        /**
         * android application plugins
         * applied in :app module
         */
        create("androidApplication") {
            id = "com.emotionstorage.convention.android.application"
            implementationClass = "com.emotionstorage.convention.AndroidApplicationConventionPlugin"
        }
        create("androidApplicationCompose") {
            id = "com.emotionstorage.convention.android.application.compose"
            implementationClass = "com.emotionstorage.convention.AndroidApplicationComposeConventionPlugin"
        }
        create("androidApplicationHilt") {
            id = "com.emotionstorage.convention.android.application.hilt"
            implementationClass = "com.emotionstorage.convention.AndroidApplicationHiltConventionPlugin"
        }

        /**
         * android library plugins
         * applied in :feat modules
         */
        create("androidLibrary") {
            id = "com.emotionstorage.convention.android.library"
            implementationClass = "com.emotionstorage.convention.AndroidLibraryConventionPlugin"
        }
        create("androidLibraryCompose") {
            id = "com.emotionstorage.convention.android.library.compose"
            implementationClass = "com.emotionstorage.convention.AndroidLibraryComposeConventionPlugin"
        }
        create("androidLibraryHilt") {
            id = "com.emotionstorage.convention.android.library.hilt"
            implementationClass = "com.emotionstorage.convention.AndroidLibraryHiltConventionPlugin"
        }
        create("androidLibraryRoom") {
            id = "com.emotionstorage.convention.android.library.room"
            implementationClass = "com.emotionstorage.convention.AndroidLibraryRoomConventionPlugin"
        }

        /**
         * java kotlin library plugins
         */
        create("javaKotlinLibrary"){
            id = "com.emotionstorage.convention.kotlin.library"
            implementationClass = "com.emotionstorage.convention.JavaKotlinLibraryConventionPlugin"
        }
        create("hilt"){
            id = "com.emotionstorage.convention.kotlin.library.hilt"
            implementationClass = "com.emotionstorage.convention.HiltConventionPlugin"
        }
        create("retrofit"){
            id = "com.emotionstorage.convention.kotlin.library.retrofit"
            implementationClass = "com.emotionstorage.convention.RetrofitConventionPlugin"
        }
    }
}