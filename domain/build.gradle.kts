plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-test-fixtures")
}

dependencies {
    implementation(libs.androidx.paging.common)

    implementation(libs.javax.inject)

    api(libs.kotlinx.coroutines.core)
}