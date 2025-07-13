plugins {
    alias(libs.plugins.sjy.lib)
    alias(libs.plugins.sjy.compose)
    kotlin("plugin.serialization") version sjy.versions.kotlin.core.get()
}

android {
    namespace = "com.carissa.revibes.home"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":core"))
}
