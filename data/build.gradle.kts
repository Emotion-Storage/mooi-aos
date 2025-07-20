plugins {
    id("com.emotionstorage.convention.android.library")
    id("com.emotionstorage.convention.android.library.hilt")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
}