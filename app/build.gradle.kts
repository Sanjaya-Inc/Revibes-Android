plugins {
    id("com.sanjaya.buildlogic.app")
    id("com.sanjaya.buildlogic.compose")
}

android {
    namespace = "com.carissa.revibes"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.carissa.revibes"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.compile.sdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.code.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.version.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.version.get())
    }
//    kotlinOptions {
//        jvmTarget = libs.versions.java.version.get()
//    }
//    buildFeatures {
//        compose = true
//    }
}

dependencies {
    implementation(core.androidx.core.ktx)
    implementation(ui.androidx.lifecycle.runtime.ktx)
    implementation(platform(ui.androidx.compose.bom))
    implementation(ui.bundles.androidx.compose)
    androidTestImplementation(platform(ui.androidx.compose.bom))
    androidTestImplementation(ui.bundles.androidx.compose.test)
    debugImplementation(ui.bundles.androidx.compose.debug)

    implementation(core.bundles.coroutines)

    implementation(ui.bundles.orbit.mvi)

    implementation(platform(core.koin.bom))
    implementation(core.bundles.koin.android)
    implementation(core.koin.annotation)
    ksp(core.koin.ksp)
    testImplementation(core.bundles.koin.test)
    testImplementation(ui.bundles.orbit.test)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}