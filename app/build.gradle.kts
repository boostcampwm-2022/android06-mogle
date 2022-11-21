import com.wakeup.buildsrc.Depends

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.wakeup.mogle"
    compileSdk = Depends.Versions.androidCompileSdkVersion

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.wakeup.mogle"
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion
        versionCode = Depends.Versions.appVersionCode
        versionName = Depends.generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.material)
    testImplementation(Depends.Libraries.junit)
    androidTestImplementation(Depends.Libraries.espresso_core)

    // Hilt
    implementation(Depends.Libraries.hilt_android)
    kapt(Depends.Libraries.hilt_android_compiler)

    // Coroutine
    implementation(Depends.Libraries.coroutines_core)

    // Timber
    implementation(Depends.Libraries.timber)

    // Firebase
    implementation(platform(Depends.Libraries.firebase_bom))
    implementation(Depends.Libraries.firebase_analytics)

    // Stetho
    implementation(Depends.Libraries.stetho)
    implementation(Depends.Libraries.stetho_okhttp3)
}
