import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.sjy.lib)
    alias(libs.plugins.sjy.compose)
    kotlin("plugin.serialization") version sjy.versions.kotlin.core.get()
    alias(sjy.plugins.lumo)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.carissa.revibes.core"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "MOCK_BASE_URL",
            "${localProperties.getProperty("MOCK_BASE_URL")}"
        )

        buildConfigField(
            "String",
            "PROD_BASE_URL",
            "${localProperties.getProperty("PROD_BASE_URL")}"
        )
    }
    buildFeatures {
        buildConfig = true
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

dependencies {
    api(sjy.androidx.lifecycle.runtime.ktx)
    api(sjy.bundles.orbit.mvi)

    debugImplementation(sjy.chucker)
    releaseImplementation(sjy.chucker.no.op)

    implementation(platform(sjy.firebase.bom))
    implementation(sjy.firebase.messaging)

    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
}
