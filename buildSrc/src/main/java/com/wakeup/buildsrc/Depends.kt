package com.wakeup.buildsrc

import com.wakeup.buildsrc.Depends.Versions.appVersionCode

object Depends {

    object Versions {
        const val appVersionCode = 1_003_000
        const val gradleVersion = "7.3.1"
        const val androidCompileSdkVersion = 32
        const val targetSdkVersion = 32
        const val minSdkVersion = 23
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val constraintLayoutVersion = "2.1.4"
        const val supportVersion = "1.5.1"
        const val materialVersion = "1.6.1"
        const val coreKtxVersion = "1.7.0"
        const val junitVersion = "4.13.2"
        const val testExtJunitVersion = "1.1.3"
        const val espressoVersion = "3.4.0"
        const val application = "7.3.1"
        const val library = "7.3.1"
        const val kotlinGradlePluginVersion = "1.7.20"
        const val googleServicesVersion = "4.3.14"

        const val navigationVersion = "2.5.2"
        const val hiltVersion = "2.44"
        const val roomVersion = "2.4.3"
        const val coroutinesCoreVersion = "1.5.0"
        const val coroutinesAndroidVersion = "1.6.0"
        const val kotlinxSerializationJsonVersion = "1.3.2"
        const val retrofitVersion = "2.9.0"
        const val retrofitSerializationVersion = "0.8.0"
        const val okhttpVersion = "4.10.0"
        const val glideVersion = "4.13.0"
        const val timberVersion = "5.0.1"
        const val pagingVersion = "3.1.1"
        const val firebaseBomVersion = "31.0.3"
        const val sandwichVersion = "1.3.2"
        const val jsoupVersion = "1.14.2"
        const val splashVersion = "1.0.0"

        const val testCoreKtxVersion = "1.5.0"
        const val testJunitKtxVersion = "1.1.4"
        const val testRunnerVersion = "1.5.1"

        const val playServiceLocationVersion = "20.0.0"
        const val naverMapVersion = "3.16.0"

        const val circleImageViewVersion = "3.1.0"

        const val stethoVersion = "1.6.0"

        const val lottieVersion = "5.2.0"
    }

    object ClassPaths {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradleVersion}"
        const val kotlin_gradle_plugin =
            "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradlePluginVersion}"
        const val navigation_gradle_plugin =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"
        const val hilt_android_gradle_plugin =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
        const val kotlinx_serialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlinGradlePluginVersion}"
        const val google_services =
            "com.google.gms:google-services:${Versions.googleServicesVersion}"
    }

    object Libraries {
        const val android_core_ktx =
            "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.supportVersion}"
        const val material = "com.google.android.material:material:${Versions.materialVersion}"
        const val constraintlayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
        const val test_ext_junit = "androidx.test.ext:junit:${Versions.testExtJunitVersion}"
        const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"

        // Junit4 test
        const val junit = "junit:junit:${Versions.junitVersion}"
        const val test_core_ktx = "androidx.test:core-ktx:${Versions.testCoreKtxVersion}"
        const val test_ext_junit_ktx = "androidx.test.ext:junit-ktx:${Versions.testJunitKtxVersion}"
        const val test_runner = "androidx.test:runner:${Versions.testRunnerVersion}"
        const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.testCoreKtxVersion}"

        // Navigation
        const val navigation_fragment_ktx =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
        const val navigation_ui_ktx =
            "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"

        // Hilt
        const val hilt_android =
            "com.google.dagger:hilt-android:${Versions.hiltVersion}"
        const val hilt_android_compiler =
            "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"

        // Room
        const val room_runtime = "androidx.room:room-runtime:${Versions.roomVersion}"
        const val room_compiler = "androidx.room:room-compiler:${Versions.roomVersion}"
        const val room_paging = "androidx.room:room-paging:${Versions.roomVersion}"
        const val room_ktx = "androidx.room:room-ktx:${Versions.roomVersion}"

        // Coroutines
        const val coroutines_core =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCoreVersion}"
        const val coroutines_android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroidVersion}"

        // Retrofit
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
        const val retrofit2_kotlinx_serialization_converter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitSerializationVersion}"

        // KotlinxSerialization
        const val kotlinx_serialization_json = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJsonVersion}"
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinGradlePluginVersion}"

        // Glide
        const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
        const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"

        // Okhttp
        const val okhttp3_logging_interceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}"

        // Timber
        const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"

        // Paging3
        const val paging_runtime_ktx = "androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}"
        const val paging_common = "androidx.paging:paging-common:${Versions.pagingVersion}"

        // Firebase
        const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebaseBomVersion}"
        const val firebase_analytics = "com.google.firebase:firebase-analytics-ktx"

        // Sandwich
        const val sandwich = "com.github.skydoves:sandwich:${Versions.sandwichVersion}"

        // Google Play Service Location
        const val play_service_location = "com.google.android.gms:play-services-location:${Versions.playServiceLocationVersion}"

        // Naver Map
        const val naverMap = "com.naver.maps:map-sdk:${Versions.naverMapVersion}"

        // Circle Image View
        const val circle_image_view = "de.hdodenhof:circleimageview:${Versions.circleImageViewVersion}"

        // Stetho
        const val stetho = "com.facebook.stetho:stetho:${Versions.stethoVersion}"
        const val stetho_okhttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stethoVersion}"

        // Jsoup
        const val jsoup = "org.jsoup:jsoup:${Versions.jsoupVersion}"

        // Splash
        const val splash = "androidx.core:core-splashscreen:${Versions.splashVersion}"

        // Lottie
        const val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"
    }

    fun generateVersionName(): String {
        val patch: Int = appVersionCode.rem(1000)
        val minor: Int = (appVersionCode / 1000).rem(1000)
        val major: Int = (appVersionCode / 1000000).rem(1000)

        return "$major.$minor.$patch"
    }
}
