import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.sanjaya.buildlogic.lib")
    id("com.sanjaya.buildlogic.compose")
    alias(ui.plugins.lumo)
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
    api(ui.androidx.lifecycle.runtime.ktx)
    api(ui.bundles.orbit.mvi)

    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
}
