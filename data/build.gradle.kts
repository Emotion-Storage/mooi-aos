plugins {
    id("com.emotionstorage.convention.library")
    id("com.emotionstorage.convention.hilt")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.common)
}