import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.wakeup.buildsrc.Depends

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.wakeup.data"
    compileSdk = Depends.Versions.androidCompileSdkVersion

    defaultConfig {
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion

        testInstrumentationRunner = Depends.Versions.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")


        buildConfigField(
            type = "String",
            name = "KAKAO_REST_API_KEY",
            value = gradleLocalProperties(rootDir).getProperty("KAKAO_REST_API_KEY")
        )
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
}

dependencies {
    implementation(project(":domain"))

    // Hilt
    implementation(Depends.Libraries.hilt_android)
    kapt(Depends.Libraries.hilt_android_compiler)

    // Room
    implementation(Depends.Libraries.room_runtime)
    implementation(Depends.Libraries.room_ktx)
    kapt(Depends.Libraries.room_compiler)

    // Network
    implementation(Depends.Libraries.retrofit)
    implementation(Depends.Libraries.okhttp3_logging_interceptor)
    implementation(Depends.Libraries.kotlinx_serialization_json)
    implementation(Depends.Libraries.retrofit2_kotlinx_serialization_converter)

    // Coroutine
    implementation(Depends.Libraries.coroutines_core)

    // Timber
    implementation(Depends.Libraries.timber)

    // Paging
    implementation(Depends.Libraries.paging_runtime_ktx)
}