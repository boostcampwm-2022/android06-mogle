import com.wakeup.buildsrc.Depends

plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Inject
    implementation("javax.inject:javax.inject:1")

    // Coroutine
    implementation(Depends.Libraries.coroutines_core)

    // Paging
    implementation(Depends.Libraries.paging_common)
}