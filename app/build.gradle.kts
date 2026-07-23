import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.compose.compiler)
}

val majorVersion = 1
val minorVersion = 0
val patchVersion = 0

fun versionComposition() = listOf(majorVersion, minorVersion, patchVersion)
fun versionName() = versionComposition().joinToString(".")
fun versionCode() = versionComposition().joinToString("").toInt()

android.applicationVariants.all {
    outputs.forEach { output ->
        if (output is BaseVariantOutputImpl) {
            output.outputFileName =
                "RmCalculator-${name}-${versionName}.${output.outputFile.extension}"
        }
    }
}

extensions.configure<ApplicationExtension>("android") {
    namespace = "com.denisdev.rmcalculator"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.denisdev.rmcalculator"
        minSdk = 29
        targetSdk = 37
        versionCode = versionCode()
        versionName = versionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.animationsDisabled = true
}

kotlin {
    jvmToolchain(17)
}

kapt { correctErrorTypes = true }

dependencies {
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":repository"))

    implementation(platform(libs.firebaseBOM))
    implementation(libs.firebaseCrashlytics)

    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    implementation(libs.coroutines)
    testImplementation(libs.testCoroutines)
    testImplementation(libs.junit)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.junit.ktx)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.test.runner.lib)
    androidTestImplementation(libs.test.monitor.lib)
    implementation(libs.concurrent.futures)

    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    androidTestImplementation(libs.hiltAndroidTesting)
    kaptAndroidTest(libs.hiltCompiler)

    testImplementation(libs.hiltAndroidTesting)
    kaptTest(libs.hiltCompiler)


    implementation(platform(libs.compose.bom))
    androidTestImplementation(libs.composeTest)
    debugImplementation(libs.composeTestRule)
}