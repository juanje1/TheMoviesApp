plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.paging.common)

    implementation(libs.javax.inject)
}