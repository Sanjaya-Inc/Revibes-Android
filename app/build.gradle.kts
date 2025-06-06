plugins {
    id("com.sanjaya.buildlogic.app")
    id("com.sanjaya.buildlogic.compose")
    id("com.sanjaya.buildlogic.firebase")
}

android {
    namespace = "com.carissa.revibes"

    defaultConfig {
        applicationId = "com.carissa.revibes"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
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
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:auth"))
    implementation(project(":features:onboarding"))
}