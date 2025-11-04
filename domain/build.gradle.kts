plugins {
    id("com.emotionstorage.convention.kotlin.library")
    id("com.emotionstorage.convention.kotlin.library.hilt")
}
dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.paging.common)
}
