import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
}

extensions.configure<LibraryExtension>("android") {
    namespace = "com.denisdev.domain"
    compileSdk = 37

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    implementation(libs.coroutines)
    testImplementation(libs.junit)
    testImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.junit.ktx)
}