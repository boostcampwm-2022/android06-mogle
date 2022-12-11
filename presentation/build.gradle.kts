import com.wakeup.buildsrc.Depends

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.wakeup.presentation"
    compileSdk = Depends.Versions.androidCompileSdkVersion

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion

        testInstrumentationRunner = Depends.Versions.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    lint {
        ignoreWarnings = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.appcompat)
    implementation(Depends.Libraries.material)
    implementation(Depends.Libraries.constraintlayout)
    androidTestImplementation(Depends.Libraries.test_ext_junit)
    androidTestImplementation(Depends.Libraries.espresso_core)
    testImplementation(Depends.Libraries.junit)

    // Navigation component
    implementation(Depends.Libraries.navigation_ui_ktx)
    implementation(Depends.Libraries.navigation_fragment_ktx)

    // Hilt
    implementation(Depends.Libraries.hilt_android)
    kapt(Depends.Libraries.hilt_android_compiler)

    // Glide
    implementation(Depends.Libraries.glide)
    implementation(Depends.Libraries.glide_compiler)

    // Timber
    implementation(Depends.Libraries.timber)

    // Coroutine
    implementation(Depends.Libraries.coroutines_android)
    implementation(Depends.Libraries.kotlinx_serialization_json)

    // Paging
    implementation(Depends.Libraries.paging_runtime_ktx)

    // Naver Map
    implementation(Depends.Libraries.naverMap)

    // Google Play Location
    implementation(Depends.Libraries.play_service_location)

    // Circle Image View
    implementation(Depends.Libraries.circle_image_view)

    // Jsoup
    implementation(Depends.Libraries.jsoup)

    // Splash
    implementation(Depends.Libraries.splash)

    // Lottie
    implementation(Depends.Libraries.lottie)
}