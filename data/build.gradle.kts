plugins {
    id("com.emotionstorage.convention.kotlin.library")
    id("com.emotionstorage.convention.kotlin.library.hilt")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
}