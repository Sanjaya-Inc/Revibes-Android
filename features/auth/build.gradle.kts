plugins {
    alias(libs.plugins.sjy.lib)
    alias(libs.plugins.sjy.compose)
}

android {
    namespace = "com.carissa.revibes.auth"
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
    implementation(platform(sjy.firebase.bom))
    implementation(sjy.firebase.messaging)
}
