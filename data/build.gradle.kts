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

        buildConfigField(
            type = "String",
            name = "WEATHER_API_KEY",
            value = gradleLocalProperties(rootDir).getProperty("WEATHER_API_KEY")
        )

        buildConfigField(
            type = "String",
            name = "KAKAO_BASE_URL",
            value ="\"https://dapi.kakao.com/\""
        )

        buildConfigField(
            type = "String",
            name = "WEATHER_BASE_URL",
            value ="\"https://api.openweathermap.org/\""
        )

        buildConfigField(
            type = "String",
            name = "WEATHER_ICON_BASE_URL",
            value ="\"https://openweathermap.org/\""
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
    
    testImplementation(Depends.Libraries.junit)
    
    // Junit4 test
    androidTestImplementation(Depends.Libraries.test_core_ktx)
    androidTestImplementation(Depends.Libraries.test_ext_junit_ktx)
    androidTestImplementation(Depends.Libraries.test_runner)
    androidTestImplementation(Depends.Libraries.coroutines_test)
    androidTestImplementation(Depends.Libraries.junit)

    // Hilt
    implementation(Depends.Libraries.hilt_android)

    kapt(Depends.Libraries.hilt_android_compiler)

    // Room
    implementation(Depends.Libraries.room_runtime)
    implementation(Depends.Libraries.room_ktx)
    implementation(Depends.Libraries.room_paging)
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
    implementation(Depends.Libraries.paging_runtime_ktx) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }

    // Glide
    implementation(Depends.Libraries.glide)
    implementation(Depends.Libraries.glide_compiler)

    // Stetho
    implementation(Depends.Libraries.stetho)
    implementation(Depends.Libraries.stetho_okhttp3)
}
